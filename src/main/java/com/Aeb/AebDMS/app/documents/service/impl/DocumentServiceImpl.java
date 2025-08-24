package com.Aeb.AebDMS.app.documents.service.impl;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.documents.dto.MetadataTransferee;
import com.Aeb.AebDMS.app.documents.dto.req.*;
import com.Aeb.AebDMS.app.documents.dto.res.TypeShareAccessDocumentRes;
import com.Aeb.AebDMS.app.documents.model.Document;
import com.Aeb.AebDMS.app.documents.model.DocumentClosure;
import com.Aeb.AebDMS.app.documents.model.DocumentPermission;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.documents.repository.DocumentClosureRepository;
import com.Aeb.AebDMS.app.documents.repository.DocumentPermissionRepository;
import com.Aeb.AebDMS.app.documents.repository.DocumentVersionRepository;
import com.Aeb.AebDMS.app.documents.service.IDocumentService;
import com.Aeb.AebDMS.app.documents.repository.DocumentRepository;
import com.Aeb.AebDMS.app.filing_categories.model.*;
import com.Aeb.AebDMS.app.filing_categories.repository.FilingCategoryRepository;
import com.Aeb.AebDMS.app.filing_categories.repository.MetadataRepository;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.model.FolderPermission;
import com.Aeb.AebDMS.app.folders.model.FolderPermissionId;
import com.Aeb.AebDMS.app.folders.repository.FolderClosureRepository;
import com.Aeb.AebDMS.app.folders.repository.FolderRepository;
import com.Aeb.AebDMS.app.folders.service.IFolderService;
import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import com.Aeb.AebDMS.shared.util.*;
import com.Aeb.AebDMS.shared.util.MinIo.MinioService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements IDocumentService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final PermissionChecker permissionChecker;
    private final FolderClosureRepository closureRepo;
    private final IKeycloakUserService keycloakService;
    private final FilingCategoryRepository filingCategoryRepository;
    private final MetadataRepository metadataRepository;
    private final AsyncIndexingService asyncIndexingService;
    private final MinioService minioService;
    private final IFolderService folderService;
    private final DocumentPermissionRepository documentPermissionRepository;
    private final DocumentClosureRepository documentClosureRepository;

    @Override
    @Transactional
    public DocumentVersion uploadDocument(DocumentUploadRequestDto request, List<FilingCategoryDocDto> filingCategoriesDto) {
        Folder folder =folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new BaseException("folder not found id:"+request.getFolderId(), HttpStatus.NOT_FOUND));
        if(!canUpload(folder,request.getCreatedBy())) throw new BaseException("access denied", HttpStatus.FORBIDDEN);

        List<FilingCategory> filingCategories = filingCategoryRepository.findAllById(filingCategoriesDto.stream().map(FilingCategoryDocDto::getId).collect(Collectors.toList()));
        if(filingCategories.size()!=filingCategoriesDto.size()) throw new BaseException("filing categories don't match", HttpStatus.BAD_REQUEST);

        List<MetadataTransferee> newCategories = checkAndFilterMetaData(filingCategoriesDto,filingCategories);


        MultipartFile file = request.getFile();

        // Extract metadata from file
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        String mimeType = file.getContentType();

        String owner = Objects.equals(folder.getOwnedBy(), request.getCreatedBy()) ?request.getCreatedBy() : folder.getOwnedBy();
        // Create Document
        Document document = Document.builder()
                .folder(folder)
                .name(originalFilename)
                .createdBy(request.getCreatedBy())
                .ownedBy(owner)
                .build();

        document = documentRepository.save(document);

        Document finalDocument = document;

        List<Metadata> metadataList = newCategories.stream()
                .map(metaData ->  Metadata.builder()
                                .document(finalDocument)
                                .definition(CategoryMetadataDefinition.builder().id(metaData.getMetadataId()).build())
                                .value(metaData.getValue())
                                .build()

                )
                .collect(Collectors.toList());

        metadataRepository.saveAll(metadataList);

        // Create DocumentVersion
        DocumentVersion version = DocumentVersion.builder()
                .document(document)
                .sizeBytes(size)
                .mimeType(mimeType)
                .versionNumber(1L)
                .build();

        version = documentVersionRepository.save(version);

        String uploadedFile = minioService.upload(file,version.getId()+'_'+originalFilename);

        asyncIndexingService.processAsyncIndexing(owner,uploadedFile,file.getOriginalFilename(),file.getContentType(),request.getLang(),newCategories,version,folder);
        return version;
    }


    private boolean canUpload(Folder folder,String userId) {
        if(permissionChecker.hasAuthority(Permissions.DOCUMENT_WRITE))return true;
        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionUploadAndUserId(folder.getId(),ids);
    }

    private List<MetadataTransferee> checkAndFilterMetaData(
            List<FilingCategoryDocDto> filingCategoriesDto,
            List<FilingCategory> filingCategories) {

        Map<Long, FilingCategory> categoryMap = filingCategories.stream()
                .collect(Collectors.toMap(FilingCategory::getId, Function.identity()));

        List<MetadataTransferee> validTransfers = new ArrayList<>();

        for (FilingCategoryDocDto categoryDto : filingCategoriesDto) {
            FilingCategory matchedCategory = categoryMap.get(categoryDto.getId());
            if (matchedCategory == null) continue;

            Map<Long, MetaDataDto> metaDtoMap = categoryDto.getMetaDataDto().stream()
                    .collect(Collectors.toMap(MetaDataDto::getId, Function.identity()));

            for (CategoryMetadataDefinition metadata : matchedCategory.getMetadataDefinitions()) {
                MetaDataDto metaDto = metaDtoMap.get(metadata.getId());

                if (metadata.isMandatory()) {
                    if (metaDto == null || metaDto.getValue() == null || metaDto.getValue().isBlank()) {
                        throw new BaseException(
                                metadata.getKey() + " of category " + matchedCategory.getName() + " is required",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                }
                if (metaDto != null && metaDto.getValue() != null && !metaDto.getValue().isBlank()) {
                    if(!metadata.getDataType().equals(MetadataType.LIST)) {
                        checkTypeData(metaDto.getValue(),metadata.getDataType());
                    }else {
                        ListMetaData list = metadata.getList();
                        if (list == null) {
                            throw new BaseException("problem in List", HttpStatus.CONFLICT);
                        }
                        if(!list.isIncludeOtherValue()){
                            if(!list.getOptions().stream().map(ListMetadataOption::getName).toList().contains(metaDto.getValue()))
                                throw new BaseException("Invalid list option: " + metaDto.getValue(), HttpStatus.BAD_REQUEST);
                        }
                    }

                     // Only add if meta exists

                    validTransfers.add(MetadataTransferee.builder()
                            .metaDataName(metadata.getKey())
                            .value(metaDto.getValue())
                            .categoryName(matchedCategory.getName())
                            .categoryId(matchedCategory.getId())
                            .metadataId(metadata.getId())
                            // .documentId(null) // Set later if needed
                            .build());
                }
            }
        }

        return validTransfers;
    }


    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Page<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Override
    public DocumentVersion findById(Long id,String userId) {
        DocumentVersion document = documentVersionRepository.findFirstByDocumentIdAndDocumentDeletedAtIsNullOrderByVersionNumberDesc(id).orElseThrow(()->
                new BaseException("folder not found",HttpStatus.NOT_FOUND));
        if(canRead(document.getDocument().getFolder(),userId,document.getDocumentId()))return document;
        throw new BaseException("folder not found",HttpStatus.NOT_FOUND);

    }

    public boolean canRead(Folder folder,String userId,Long docId) {
        if(permissionChecker.hasAuthority(Permissions.DOCUMENT_READ))return true;

        if(userId.equals(folder.getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionReadAndUserId(folder.getId(),ids) ||
                documentRepository.existsByDocumentIdAndPermissionReadAndUserId(docId,ids);
    }

    @Override
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public DocumentVersion updateDocumentVersion(DocumentVersion document) {
        return documentVersionRepository.save(document);
    }

    @Override
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    private void checkTypeData(String value, MetadataType dataType) throws BaseException {
        if (value == null || value.trim().isEmpty()) {
            throw new BaseException("Value cannot be null or empty",HttpStatus.BAD_REQUEST);
        }

        String trimmed = value.trim();

        try {
            switch (dataType) {
                case STRING:
                    // Always valid (you may add length limits if needed)
                    break;

                case NUMBER:
                    if (!trimmed.matches("-?\\d+")) {
                        throw new BaseException("Value must be a valid integer number",HttpStatus.BAD_REQUEST);
                    }
                    break;

                case FLOAT:
                    if (!trimmed.matches("-?\\d+(\\.\\d+)?")) {
                        throw new BaseException("Value must be a valid floating-point number",HttpStatus.BAD_REQUEST);
                    }
                    break;

                case BOOLEAN:
                    if (!trimmed.equalsIgnoreCase("true") && !trimmed.equalsIgnoreCase("false")) {
                        throw new BaseException("Value must be 'true' or 'false'",HttpStatus.BAD_REQUEST);
                    }
                    break;

                case DATE:
                    try {
                        LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (DateTimeParseException e) {
                        throw new BaseException("Value must be a valid date in format yyyy-MM-dd",HttpStatus.BAD_REQUEST);
                    }
                    break;

                case DATETIME:
                    try {
                        LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } catch (DateTimeParseException e) {
                        throw new BaseException("Value must be a valid datetime in format yyyy-MM-dd HH:mm:ss",HttpStatus.BAD_REQUEST);
                    }
                    break;

                default:
                    throw new BaseException("Unsupported MetadataType: " + dataType,HttpStatus.BAD_REQUEST);
            }
        } catch (BaseException e) {
            throw e; // rethrow as-is
        } catch (Exception e) {
            throw new BaseException("Error validating value for type " + dataType + ": " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void reName(Long id, String userId, String name) {
        DocumentVersion document = findById(id, userId);
        if(!haveAccessEdit(document,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);
        document.getDocument().setName(name);
        try {
            updateDocument(document.getDocument());
        }catch (Exception e){
            throw new BaseException("Failed to update document",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void move(Long id, String userId, Long desId) {
        DocumentVersion documentVersion = findById(id, userId);
        if(!haveAccessEdit(documentVersion,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        Folder newParentFolder = folderService.findById(desId, userId,true);

        if(!canUpload(newParentFolder,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        if(documentRepository.existsByFolderIdAndDocName(newParentFolder.getId(),documentVersion.getDocument().getName()))
            throw new BaseException("document already exists",HttpStatus.CONFLICT);

        documentVersion.getDocument().setFolder(newParentFolder);
        documentVersion.getDocument().setOwnedBy(newParentFolder.getOwnedBy());
        try {
            updateDocument(documentVersion.getDocument());
        }catch (Exception e){
            throw new BaseException("Failed to update folder",HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean haveAccessEdit(DocumentVersion document,String userId) {
        if(permissionChecker.hasAuthority(Permissions.DOCUMENT_EDIT))return true;
        if(userId.equals(document.getDocument().getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionEditDocAndUserId(document.getDocument().getFolderId(),ids) ||
                documentRepository.existsByDocumentIdAndPermissionEditAndUserId(document.getDocument().getId(),ids);
    }

    @Override
    @Transactional
    public void deleteById(Long id, String userId) {
        DocumentVersion documentVersion = findById(id, userId);
        Instant time =Instant.now();

        if(!haveAccessDelete(documentVersion,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        documentVersion.getDocument().setDeletedAt(time);
        documentRepository.save(documentVersion.getDocument());
    }

    @Override
    public Page<TypeShareAccessDocumentRes> getAllPermission(long documentId, String userId, Pageable page){

        DocumentVersion documentVersion = findById(documentId, userId);

        if(!haveAccessManagePermissions(documentVersion,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        Page<DocumentPermission> grants = documentRepository.getPermissionsByDocumentId(documentId,page);

        Map<String, UserDto> users = keycloakService.getUsersByIds(grants.getContent().stream()
                .filter(g->g.getGranteeType().equals(GranteeType.USER))
                .map(DocumentPermission::getGranteeId)
                .toList());
        Map<String, GroupDto> groups = keycloakService.getGroupsByIds(grants.getContent().stream()
                .filter(g->g.getGranteeType().equals(GranteeType.GROUP))
                .map(DocumentPermission::getGranteeId)
                .toList());
        Map<String, RoleDto> roles = keycloakService.getRolesByIds(grants.getContent().stream()
                .filter(g->g.getGranteeType().equals(GranteeType.ROLE))
                .map(DocumentPermission::getGranteeId)
                .toList());

        List<TypeShareAccessDocumentRes> granteeWithPermissions = new ArrayList<>();
        grants.getContent().forEach(g->{
            if(g.getGranteeType().equals(GranteeType.USER))
                granteeWithPermissions.add(new TypeShareAccessDocumentRes(users.get(g.getGranteeId()),buildPermissionRes(g)));
            else if(g.getGranteeType().equals(GranteeType.GROUP))
                granteeWithPermissions.add(new TypeShareAccessDocumentRes(groups.get(g.getGranteeId()),buildPermissionRes(g)));
            else
                granteeWithPermissions.add(new TypeShareAccessDocumentRes(roles.get(g.getGranteeId()),buildPermissionRes(g)));
        });

        return new PageImpl<>(granteeWithPermissions,page,grants.getTotalElements());
    }

    private DocumentPermissionReq buildPermissionRes(DocumentPermission dper){
        return DocumentPermissionReq.builder()
                .canDelete(dper.isCanDelete())
                .canEdit(dper.isCanEdit())
                .canManagePermissions(dper.isCanManagePermissions())
                .canView(dper.isCanView())
                .build();

    }

    public Boolean haveAccessDelete(DocumentVersion document,String userId) {
        if(permissionChecker.hasAuthority(Permissions.DOCUMENT_EDIT))return true;
        if(userId.equals(document.getDocument().getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionDeleteDocAndUserId(document.getDocument().getFolderId(),ids) ||
                documentRepository.existsByDocumentIdAndPermissionDeleteAndUserId(document.getDocument().getId(),ids);
    }

    @Override
    @Transactional
    public TypeShareAccessDocumentRes createOrUpdatePermission(long documentId, String userId, GranteeType granteeType,
                                                               String granteeId, DocumentPermissionReq permissionReq){
        if(userId.equals(granteeId))
            throw new BaseException("Access denied", HttpStatus.FORBIDDEN);

        DocumentVersion documentVersion = findById(documentId, userId);
        AtomicReference<Boolean> isNew = new AtomicReference<>(false);

        if(!haveAccessManagePermissions(documentVersion,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        DocumentPermission documentPermission = documentRepository.getPermissionByGranteeIdAndDocumentId(documentId,granteeId).orElseGet(()->{
                    isNew.set(true);
                    return buildPermission(new TypeShareAccesDoc(granteeId,permissionReq),granteeType);
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

        if(!isNew.get()){
            updatePermission(documentPermission,permissionReq);
            documentVersion.getDocument().setPublic(true);
            updateDocument(documentVersion.getDocument());
        }

        documentPermission = documentPermissionRepository.save(documentPermission);
        documentClosureRepository.save(DocumentClosure.builder()
                        .permissionId(documentPermission.getId())
                        .documentId(documentId)
                .build());

        return new TypeShareAccessDocumentRes(granteeObject,permissionReq);
    }

    private DocumentPermission buildPermission(TypeShareAccesDoc typeShareAccesDoc, GranteeType granteeType) {

        return DocumentPermission.builder()
                .granteeId(typeShareAccesDoc.getId())
                .granteeType(granteeType)
                .canDelete(typeShareAccesDoc.getPermission().isCanDelete())
                .canEdit(typeShareAccesDoc.getPermission().isCanEdit())
                .canView(typeShareAccesDoc.getPermission().isCanView())
                .canManagePermissions(typeShareAccesDoc.getPermission().isCanManagePermissions())
                .build();
    }

    private void updatePermission(DocumentPermission documentPermission, DocumentPermissionReq permissionReq) {
        documentPermission.setCanView(permissionReq.isCanView());
        documentPermission.setCanEdit(permissionReq.isCanEdit());
        documentPermission.setCanDelete(permissionReq.isCanDelete());
        documentPermission.setCanManagePermissions(permissionReq.isCanManagePermissions());
    }

    @Override
    public void deletePermission(long documentId, String userId, String granteeId){

        DocumentVersion documentVersion = findById(documentId, userId);

        if(!haveAccessManagePermissions(documentVersion,userId))
            throw new BaseException("Access denied",HttpStatus.FORBIDDEN);

        DocumentPermission documentPermission = documentRepository.getPermissionByGranteeIdAndDocumentId(documentId,granteeId).orElseThrow(()->
                new BaseException("Grantee not found",HttpStatus.NOT_FOUND));

        documentPermissionRepository.delete(documentPermission);
        documentVersion.getDocument().setPublic(false);
        if(documentClosureRepository.countByPermissionId(documentPermission.getId())==0)
            updateDocument(documentVersion.getDocument());

    }

    public Boolean haveAccessManagePermissions(DocumentVersion document,String userId) {
        if(permissionChecker.hasAuthority(Permissions.DOCUMENT_EDIT))return true;
        if(userId.equals(document.getDocument().getOwnedBy()))return true;
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);
        return closureRepo.existsByFolderIdAndPermissionManagePermissionsDocAndUserId(document.getDocument().getFolderId(),ids) ||
                documentRepository.existsByDocumentIdAndPermissionManagePermissionsAndUserId(document.getDocument().getId(),ids);
    }


}
