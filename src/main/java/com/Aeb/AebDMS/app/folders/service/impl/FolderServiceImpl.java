package com.Aeb.AebDMS.app.folders.service.impl;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.documents.repository.DocumentRepository;
import com.Aeb.AebDMS.app.folders.dto.app.FolderGetResApp;
import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.dto.req.CreateFolderDto;
import com.Aeb.AebDMS.app.folders.dto.req.FolderPermissionReq;
import com.Aeb.AebDMS.app.folders.dto.req.TypeShareAcces;
import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import com.Aeb.AebDMS.app.elastic.service.IFolderElasticService;
import com.Aeb.AebDMS.app.folders.dto.res.GetSharedFolderResApp;
import com.Aeb.AebDMS.app.folders.dto.res.TypeShareAccessRes;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.model.FolderClosure;
import com.Aeb.AebDMS.app.folders.model.FolderPermission;
import com.Aeb.AebDMS.app.folders.model.FolderPermissionId;
import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.shared.util.GranteeType;
import com.Aeb.AebDMS.app.folders.repository.FolderClosureRepository;
import com.Aeb.AebDMS.app.folders.repository.FolderPermissionRepository;
import com.Aeb.AebDMS.app.folders.service.IFolderService;
import com.Aeb.AebDMS.app.folders.repository.FolderRepository;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import com.Aeb.AebDMS.shared.util.PageMeta;
import com.Aeb.AebDMS.shared.util.PermissionChecker;
import com.Aeb.AebDMS.shared.util.Permissions;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderServiceImpl implements IFolderService {

    private final FolderRepository           folderRepo;
    private final FolderPermissionRepository permRepo;
    private final FolderClosureRepository closureRepo;
    private final IKeycloakUserService keycloakService;
    private final PermissionChecker permissionChecker;
    private final IFolderElasticService folderElasticService;
    private final DocumentRepository documentRepository;
    private final FolderPermissionRepository folderPermissionRepository;

    @Override
    @Transactional
    public Folder createFolder(CreateFolderDto createFolderDto, String createdBy) {
        Folder parent = null;
        if (createFolderDto.getParentId() != null) {
            parent = folderRepo.findById(createFolderDto.getParentId())
                    .orElseThrow(() -> new BaseException("parent folder not found id:"+createFolderDto.getParentId(), HttpStatus.NOT_FOUND));
        }
        if(parent!=null)
            if(!haveAccessWrite(parent,createdBy))
             throw new BaseException("parent folder not accessible id:"+createFolderDto.getParentId(), HttpStatus.BAD_REQUEST);
        try {
            return buildFolder(createFolderDto,createdBy,new ArrayList<>(),parent);
        }catch (BaseException e){
            log.error(e.getMessage(),e);
            throw new BaseException("FAILED TO CREATE FOLDER ", HttpStatus.EXPECTATION_FAILED);
        }
    }

    private Boolean haveAccessWrite(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.FOLDER_CREATE))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionWriteAndUserId(folder.getId(),ids);
    }

    private Folder buildFolder(CreateFolderDto dto, String createdBy,List<FolderPermission> inheritedPermissions,Folder parentFolder) {
        // 1) save the Folder itself
        String ownedBy = parentFolder!=null?parentFolder.getOwnedBy():createdBy;
        Folder folder = Folder.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdBy(createdBy)
                .ownedBy(ownedBy)
                .parent(parentFolder)
                .build();
        folder = folderRepo.save(folder);
        List<String> grantedIds = new ArrayList<>(inheritedPermissions.stream().map(FolderPermission::getGranteeId).toList());
        grantedIds.add(ownedBy);
        if(!ownedBy.equals(createdBy))
            grantedIds.add(createdBy);

        folderElasticService.saveFolderDocument(FolderElastic.builder()
                        .name(folder.getName())
                        .id(folder.getId())
                        .description(folder.getDescription())
                        .path(folder.getPath())
                        .createdAt(folder.getCreatedAt())
                        .grantedIds(grantedIds)
                        .updatedAt(folder.getUpdatedAt())
                .build());
        // 2) create and save each permission
        List<FolderPermission> perms = permRepo.saveAll(buildPermissions(dto));
        perms.addAll(inheritedPermissions);
        // 3) build closure entries:
        List<FolderClosure> closures = new ArrayList<>();
        for (FolderPermission p : perms) {
                closures.add(FolderClosure.builder()
                                .folderId(folder.getId())
                                .permissionId(p.getId())
                        .build());


        }

        closureRepo.saveAll(closures);
        List<FolderPermission> inheritedCopy = new ArrayList<>(inheritedPermissions);
        perms.stream()
                .filter(FolderPermission::isInherits)
                .forEach(inheritedCopy::add);

        Folder finalFolder = folder;
        dto.getSubgroups().forEach(subgroup -> buildFolder(subgroup, createdBy, inheritedCopy, finalFolder));

        return folder;
    }

    private List<FolderPermission> buildPermissions(CreateFolderDto dto) {
        var userIds   = dto.getUsersGevenPermission().stream().map(TypeShareAcces::getId).toList();
        var groupIds  = dto.getGoupesGevenPermission().stream().map(TypeShareAcces::getId).toList();
        var roleIds   = dto.getRolesGevenPermission().stream().map(TypeShareAcces::getId).toList();

        var usersExist  = keycloakService.getUsersExistingByIds(userIds);
        var groupsExist = keycloakService.getGroupersExistingByIds(groupIds);
        var rolesExist  = keycloakService.getRolesExistingByIds(roleIds);

        List<FolderPermission> perms = new ArrayList<>();
        IntStream.range(0, userIds.size())
                .filter(usersExist::get)
                .mapToObj(i -> buildPermission(dto.getUsersGevenPermission().get(i), GranteeType.USER))
                .forEach(perms::add);

        IntStream.range(0, groupIds.size())
                .filter(groupsExist::get)
                .mapToObj(i -> buildPermission(dto.getGoupesGevenPermission().get(i), GranteeType.GROUP))
                .forEach(perms::add);

        IntStream.range(0, roleIds.size())
                .filter(rolesExist::get)
                .mapToObj(i -> buildPermission(dto.getRolesGevenPermission().get(i), GranteeType.ROLE))
                .forEach(perms::add);

        return perms;
    }

    private FolderPermission buildPermission(TypeShareAcces tsa, GranteeType type) {
        return FolderPermission.builder()
                .granteeType(type)
                .granteeId(tsa.getId())
                .canView(tsa.getPermission().isCanView())
                .canUpload(tsa.getPermission().isCanUpload())
                .canEdit(tsa.getPermission().isCanEdit())
                .canDelete(tsa.getPermission().isCanDelete())
                .canShare(tsa.getPermission().isCanShare())
                .canManagePermissionsDoc(tsa.getPermission().isCanManagePermissionsDoc())
                .canDeleteDoc(tsa.getPermission().isCanEdit())
                .canEditDoc(tsa.getPermission().isCanEditDoc())
                .canShareDoc(tsa.getPermission().isCanShareDoc())
                .canCreateSubFolders(tsa.getPermission().isCanCreateSubFolders())
                .inherits(tsa.getPermission().isInherits())
                .canManagePermissions(tsa.getPermission().isCanManagePermissions())
                .build();
    }

    @Override
    public Folder findById(Long id, String userId,boolean content) {
        Folder folder = folderRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()-> new BaseException("folder not fund", HttpStatus.NOT_FOUND));
       if(content){
           if(haveAccessRead(folder,userId))
               return folder;
           throw new BaseException("Access denied",HttpStatus.FORBIDDEN);
       }else if(folder.getParent()==null){
           if(userId.equals(folder.getOwnedBy()))
               return folder;
           throw new BaseException("Access denied",HttpStatus.FORBIDDEN);
       }else {
           if(haveAccessRead(folder.getParent(),userId))
               return folder;
           throw new BaseException("Access denied",HttpStatus.FORBIDDEN);
       }
    }

    @Override
    public Page<FolderWithCountDto> getMyRepo(String userId, Pageable pageable, String name) {
       if(name!=null&&!name.isEmpty())
           return folderRepo.getMyRepo(userId,pageable);
       return folderRepo.getMyRepoByName(userId,name,pageable);
    }

    @Override
    public FolderGetResApp getFullFolder(Long id, String userId, Pageable pageable, String name,Boolean showFolders) {

        Folder folder = findById(id, userId,true);
        return restGetFullFolder(folder,userId,pageable,name,showFolders);
    }

    @Override
    public FolderGetResApp getFullFolder(String path, String userId, Pageable pageable, String name,Boolean showFolders) {

        Folder folder = folderRepo.findByPathAndDeletedAtIsNull(path).orElseThrow(()-> new BaseException("folder not fund", HttpStatus.NOT_FOUND));
        if(!haveAccessRead(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        return restGetFullFolder(folder,userId,pageable,name,showFolders);
    }

    public FolderGetResApp restGetFullFolder(Folder folder, String userId, Pageable pageable, String name,Boolean showFolders) {
        // 1) fetch folders page with the requested pageable
        Page<FolderWithCountDto> folderPage = null;
        if(showFolders){
            if (name != null && !name.isEmpty())
                folderPage = folderRepo.getFolderContent(folder.getId(), pageable);
            else
                folderPage = folderRepo.getFolderContentByName(folder.getId(), name, pageable);
        }

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long folderTotal =folderPage!=null? folderPage.getTotalElements():0;

        // global start index for this merged page
        long startIndex = (long) pageNumber * pageSize;

        // how many docs to skip globally before this page
        long docGlobalOffset = Math.max(0L, startIndex - folderTotal);

        Page<DocumentVersion> docPage = null;
        long totalDocElements;
        int docInnerOffset = 0;

        // If folder page didn't fill the requested page, fetch docs slice; otherwise fetch a tiny page to get count
        if (folderTotal < pageSize) {
            // compute doc page number & inner offset
            int docPageNumber = pageSize == 0 ? 0 : (int) (docGlobalOffset / pageSize);
            docInnerOffset = pageSize == 0 ? 0 : (int) (docGlobalOffset % pageSize);

            Pageable docPageable = PageRequest.of(docPageNumber, pageSize, pageable.getSort());
            if(name!=null&&!name.isEmpty())
                docPage = documentRepository.getLatestDocumentVersionsByFolderId(folder.getId(), docPageable);
            else
                docPage = documentRepository.getLatestDocumentVersionsByFolderIdByName(folder.getId(),name, docPageable);

            totalDocElements = docPage.getTotalElements();
        } else {
            // folders fill the page: we still need total number of docs to compute totals
            Page<DocumentVersion> tmp;
            if(name!=null&&!name.isEmpty())
                tmp = documentRepository.getLatestDocumentVersionsByFolderId(folder.getId(), PageRequest.of(0, 1));
            else
                tmp = documentRepository.getLatestDocumentVersionsByFolderIdByName(folder.getId(),name, PageRequest.of(0, 1));
            totalDocElements = tmp.getTotalElements();
        }

        return buildFolderGetResApp(folder, folderPage, docPage, pageable, docInnerOffset, totalDocElements);
    }

    private FolderGetResApp buildFolderGetResApp(
            Folder folder,
            Page<FolderWithCountDto> folderPage,
            Page<DocumentVersion> docPage,
            Pageable requestedPageable,
            int docInnerOffset,                  // where to start inside docPage.getContent()
            long totalDocElements                 // total number of docs for the query (even if docPage==null)
    ) {
        int pageSize = requestedPageable.getPageSize();
        int pageNumber = requestedPageable.getPageNumber();

        List<FolderWithCountDto> foldersContent =folderPage!=null? folderPage.getContent():new ArrayList<>();
        List<DocumentVersion> documentsContent = new ArrayList<>();

        int takenFromFolders = foldersContent.size();
        int remaining = Math.max(0, pageSize - takenFromFolders);

        if (remaining > 0 && docPage != null) {
            List<DocumentVersion> docs = docPage.getContent();
            int start = Math.min(docInnerOffset, docs.size());
            documentsContent.addAll(docs.stream()
                    .skip(start)
                    .limit(remaining)
                    .toList());
        }

        long totalElements = folderPage!=null?folderPage.getTotalElements() + totalDocElements:totalDocElements;

        PageMeta meta = new PageMeta(
                totalElements,
                pageSize,
                pageNumber
        );

        return new FolderGetResApp(folder, foldersContent, documentsContent, meta);
    }

    @Override
    public GetSharedFolderResApp getSharedFolders(String userId, Pageable pageable, String name,Boolean showFolders) {

        // 1) fetch folders page with the requested pageable
        Page<FolderWithCountDto> folderPage =null;

        if(name!=null&&!name.isEmpty())
            folderPage = folderRepo.findSharedRootFolders(userId,pageable);
        else
            folderPage = folderRepo.findSharedRootFoldersByName(userId,name,pageable);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long folderTotal = folderPage!=null?folderPage.getTotalElements():0;

        // global start index for this merged page
        long startIndex = (long) pageNumber * pageSize;

        // how many docs to skip globally before this page
        long docGlobalOffset = Math.max(0L, startIndex - folderTotal);

        Page<DocumentVersion> docPage = null;
        long totalDocElements;
        int docInnerOffset = 0;

        // If folder page didn't fill the requested page, fetch docs slice; otherwise fetch a tiny page to get count
        if (folderTotal < pageSize) {
            // compute doc page number & inner offset
            int docPageNumber = pageSize == 0 ? 0 : (int) (docGlobalOffset / pageSize);
            docInnerOffset = pageSize == 0 ? 0 : (int) (docGlobalOffset % pageSize);

            Pageable docPageable = PageRequest.of(docPageNumber, pageSize, pageable.getSort());
            if(name!=null&&!name.isEmpty())
                docPage = documentRepository.findLatestSharedRootDocuments(userId, docPageable);
            else
                docPage = documentRepository.findLatestSharedRootDocumentsByName(userId,name,pageable);
            totalDocElements = docPage.getTotalElements();
        } else {
            // folders fill the page: we still need total number of docs to compute totals
            Page<DocumentVersion> tmp;
            if(name!=null&&!name.isEmpty())
                tmp = documentRepository.findLatestSharedRootDocuments(userId, PageRequest.of(0, 1));
            else
                tmp = documentRepository.findLatestSharedRootDocumentsByName(userId,name, PageRequest.of(0, 1));
            totalDocElements = tmp.getTotalElements();
        }

        return buildGetSharedFolderResApp( folderPage, docPage, pageable, docInnerOffset, totalDocElements);
    }

    private GetSharedFolderResApp buildGetSharedFolderResApp(
            Page<FolderWithCountDto> folderPage,
            Page<DocumentVersion> docPage,
            Pageable requestedPageable,
            int docInnerOffset,                  // where to start inside docPage.getContent()
            long totalDocElements                 // total number of docs for the query (even if docPage==null)
    ) {
        int pageSize = requestedPageable.getPageSize();
        int pageNumber = requestedPageable.getPageNumber();

        List<FolderWithCountDto> foldersContent = folderPage!=null?folderPage.getContent():new ArrayList<>();
        List<DocumentVersion> documentsContent = new ArrayList<>();

        int takenFromFolders = foldersContent.size();
        int remaining = Math.max(0, pageSize - takenFromFolders);

        if (remaining > 0 && docPage != null) {
            List<DocumentVersion> docs = docPage.getContent();
            int start = Math.min(docInnerOffset, docs.size());
            documentsContent.addAll(docs.stream()
                    .skip(start)
                    .limit(remaining)
                    .toList());
        }

        long totalElements = folderPage!=null?folderPage.getTotalElements() + totalDocElements:totalDocElements;

        PageMeta meta = new PageMeta(
                totalElements,
                pageSize,
                pageNumber
        );

        return new GetSharedFolderResApp( foldersContent, documentsContent, meta);
    }


    public Boolean haveAccessRead(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.FOLDER_READ))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionReadAndUserId(folder.getId(),ids);
    }

    @Override
    public void updateFolder(Folder folder) {
        folderRepo.save(folder);
    }

    @Override
    public void reName(Long id, String userId, String name) {
        Folder folder = findById(id, userId,false);
        if(!haveAccessEdit(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        folder.setName(name);
        try {
            updateFolder(folder);
        }catch (Exception e){
            throw new BaseException("Failed to update folder",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void move(Long id, String userId, Long desId) {
        Folder folder = findById(id, userId,false);
        if(!haveAccessEdit(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        Folder newParentFolder = findById(desId, userId,false);

        if(newParentFolder.getPath().startsWith(folder.getPath()))
            throw new BaseException("can not move Folder to sub folder",HttpStatus.CONFLICT);

        if(!haveAccessWrite(newParentFolder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        folder.setParent(newParentFolder);
        folder.setOwnedBy(newParentFolder.getOwnedBy());
        try {
            updateFolder(folder);
        }catch (Exception e){
            throw new BaseException("Failed to update folder",HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean haveAccessEdit(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.FOLDER_UPDATE))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionEditAndUserId(folder.getId(),ids);
    }

    @Override
    @Transactional
    public void deleteById(Long id,String userId) {
        Folder folder = findById(id, userId,false);
        Instant time =Instant.now();
        if(!haveAccessDelete(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        folder.setDeletedAt(time);
        folderRepo.save(folder);

        List<Long> folderIds = countSubFolderIds(folder).stream().toList();

        folderRepo.updateSubfoldersDeletedAt(time,folderIds);
        documentRepository.updateDocumentsDeletedAt(time,folderIds);
    }

    private Set<Long> countSubFolderIds(Folder folder) {
        Set<Long> folderIds =new HashSet<>();
        Deque<Folder> stack = new ArrayDeque<>();
        stack.push(folder);

        while (!stack.isEmpty()) {
            Folder currentFolder = stack.pop();
            folderIds.add(currentFolder.getId());
            for (Folder subfolder : currentFolder.getSubFolders()) {
                stack.push(subfolder);
            }
        }

        return folderIds;
    }

    @Override
    public Page<TypeShareAccessRes> getAllPermission(long folderId, String userId, Pageable page){

        Folder folder = findById(folderId, userId,false);
        if(!haveAccessManagePermissions(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        Page<FolderPermission> grants = permRepo.getPermissionsByFolderId(folderId,page);

        Map<String, UserDto> users = keycloakService.getUsersByIds(grants.getContent().stream()
                                            .filter(g->g.getGranteeType().equals(GranteeType.USER))
                                            .map(FolderPermission::getGranteeId)
                                            .toList());
        Map<String, GroupDto> groups = keycloakService.getGroupsByIds(grants.getContent().stream()
                                            .filter(g->g.getGranteeType().equals(GranteeType.GROUP))
                                            .map(FolderPermission::getGranteeId)
                                            .toList());
        Map<String, RoleDto> roles = keycloakService.getRolesByIds(grants.getContent().stream()
                                            .filter(g->g.getGranteeType().equals(GranteeType.ROLE))
                                            .map(FolderPermission::getGranteeId)
                                            .toList());

        List<TypeShareAccessRes> granteeWithPermissions = new ArrayList<>();
        grants.getContent().forEach(g->{
            if(g.getGranteeType().equals(GranteeType.USER))
                granteeWithPermissions.add(new TypeShareAccessRes(users.get(g.getGranteeId()),buildPermissionRes(g)));
            else if(g.getGranteeType().equals(GranteeType.GROUP))
                granteeWithPermissions.add(new TypeShareAccessRes(groups.get(g.getGranteeId()),buildPermissionRes(g)));
            else
                granteeWithPermissions.add(new TypeShareAccessRes(roles.get(g.getGranteeId()),buildPermissionRes(g)));
        });

        return new PageImpl<>(granteeWithPermissions,page,grants.getTotalElements());
    }

    private FolderPermissionReq buildPermissionRes(FolderPermission fper){
        return FolderPermissionReq.builder()
                .canCreateSubFolders(fper.isCanCreateSubFolders())
                .canDelete(fper.isCanDelete())
                .canDeleteDoc(fper.isCanDeleteDoc())
                .canEdit(fper.isCanEdit())
                .canEditDoc(fper.isCanEditDoc())
                .canManagePermissions(fper.isCanManagePermissions())
                .canManagePermissionsDoc(fper.isCanManagePermissionsDoc())
                .canShare(fper.isCanShare())
                .canShareDoc(fper.isCanShareDoc())
                .canUpload(fper.isCanUpload())
                .canView(fper.isCanView())
                .inherits(fper.isInherits())
                .build();

    }

    public Boolean haveAccessDelete(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.FOLDER_DELETE))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionDeleteAndUserId(folder.getId(),ids);
    }

    @Override
    @Transactional
    public TypeShareAccessRes createOrUpdatePermission(long folderId, String userId,GranteeType granteeType,
                                                       String granteeId, FolderPermissionReq permissionReq){
        if(userId.equals(granteeId))
            throw new BaseException("Access denied", HttpStatus.FORBIDDEN);

        Folder folder = findById(folderId, userId,false);
        AtomicReference<Boolean> isNew = new AtomicReference<>(false);
        if(!haveAccessManagePermissions(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        FolderPermission folderPermission = closureRepo.getPermissionByGranteeIdAndFolderId(folderId,granteeId).orElseGet(()->{
                    isNew.set(true);
                    return buildPermission(new TypeShareAcces(granteeId,permissionReq),granteeType);
                }
        );

        Object granteeObject;
        if(granteeType.equals(GranteeType.USER)){
            granteeObject = keycloakService.getUserById(granteeId).orElseThrow(()->
                    new BaseException("Grantee not found",HttpStatus.NOT_FOUND));
        }else if(granteeType.equals(GranteeType.GROUP)){
            granteeObject = keycloakService.getGroupById(granteeId).orElseThrow(()->
                    new BaseException("Grantee not found",HttpStatus.NOT_FOUND));
        }else{
            granteeObject = keycloakService.getRoleById(granteeId).orElseThrow(()->
                    new BaseException("Grantee not found",HttpStatus.NOT_FOUND));
        }

        if(((!isNew.get())&&folderPermission.isInherits()) != permissionReq.isInherits()){
            Set<Long> subFolders = countSubFolderIds(folder);
            if(!isNew.get())
                updatePermission(folderPermission,permissionReq);
            folderPermission = permRepo.save(folderPermission);
            FolderPermission finalFolderPermission = folderPermission;

            if(permissionReq.isInherits()){
                subFolders.add(folderId);
                List<FolderClosure> folderClosureList = subFolders.stream().map(id->
                        FolderClosure.builder()
                                .folderId(id)
                                .permissionId(finalFolderPermission.getId())
                                .build()).toList();

                closureRepo.saveAll(folderClosureList);

                folderRepo.updateFoldersIsPublic(true,subFolders.stream().toList());
            }else {
                subFolders.remove(folderId);
                closureRepo.deleteAllByFolderIdsAndPermissionId(subFolders,finalFolderPermission.getId());
                folderRepo.updateFoldersIsPublic(false,subFolders.stream().toList());
            }
        }
        else{
            if(!isNew.get()){
                updatePermission(folderPermission,permissionReq);
            }else {
                folder.setPublic(true);
                updateFolder(folder);
            }
            permRepo.save(folderPermission);
        }

        return new TypeShareAccessRes(granteeObject,permissionReq);
    }

    private void updatePermission(FolderPermission folderPermission, FolderPermissionReq permissionReq) {
        folderPermission.setCanView(permissionReq.isCanView());
        folderPermission.setCanUpload(permissionReq.isCanUpload());
        folderPermission.setCanEdit(permissionReq.isCanEdit());
        folderPermission.setCanDelete(permissionReq.isCanDelete());
        folderPermission.setCanShare(permissionReq.isCanShare());
        folderPermission.setCanManagePermissionsDoc(permissionReq.isCanManagePermissionsDoc());
        folderPermission.setCanDeleteDoc(permissionReq.isCanDeleteDoc());
        folderPermission.setCanEditDoc(permissionReq.isCanEditDoc());
        folderPermission.setCanShareDoc(permissionReq.isCanShareDoc());
        folderPermission.setCanCreateSubFolders(permissionReq.isCanCreateSubFolders());
        folderPermission.setInherits(permissionReq.isInherits());
        folderPermission.setCanManagePermissions(permissionReq.isCanManagePermissions());
    }

    @Override
    public void deletePermission(long folderId, String userId, String granteeId, boolean sub){

        Folder folder = findById(folderId, userId,false);
        if(!haveAccessManagePermissions(folder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);
        FolderPermission folderPermission = closureRepo.getPermissionByGranteeIdAndFolderId(folderId,granteeId).orElseThrow(()->
                new BaseException("Grantee not found",HttpStatus.NOT_FOUND));

        if(sub&&folderPermission.isInherits()){
            List<Long> subFolders = countSubFolderIds(folder).stream().toList();
            List<FolderPermissionId> folderPermissionIdList = subFolders.stream().map(sf-> new FolderPermissionId(sf,folderPermission.getId())).toList();
            if(!folderPermissionIdList.isEmpty())
                closureRepo.deleteAllById(folderPermissionIdList);
        }
        else
            closureRepo.deleteById(new FolderPermissionId(folderId,folderPermission.getId()));

        if(closureRepo.countByPermissionId(folderPermission.getId())==0){
            folderPermissionRepository.delete(folderPermission);
            folder.setPublic(false);
            updateFolder(folder);
        }

    }

    public Boolean haveAccessManagePermissions(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.FOLDER_CHANGE_PERMISSIONS))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionManagePermissionsAndUserId(folder.getId(),ids);
    }

}
