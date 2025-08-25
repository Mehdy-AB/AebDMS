package com.Aeb.AebDMS.app.folders.service;

import com.Aeb.AebDMS.app.folders.dto.app.FolderGetResApp;
import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.dto.req.CreateFolderDto;
import com.Aeb.AebDMS.app.folders.dto.req.FolderPermissionReq;
import com.Aeb.AebDMS.app.folders.dto.req.SortFields;
import com.Aeb.AebDMS.app.folders.dto.res.GetSharedFolderResApp;
import com.Aeb.AebDMS.app.folders.dto.res.TypeShareAccessRes;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.shared.util.GranteeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFolderService {

    Folder createFolder(CreateFolderDto createFolderDto, String createdBy);

    Folder findById(Long id, String userId,boolean content);

    void updateFolder(Folder folder);

    void reName(Long id, String userId, String name);
    void move(Long id, String userId, Long desId);


    Page<FolderWithCountDto> getMyRepo(String userId, Pageable pageable, String name);

    FolderGetResApp getFullFolder(Long id, String userId, Pageable pageable, String name,Boolean showFolders);
    FolderGetResApp getFullFolder(String path, String userId, Pageable pageable, String name,Boolean showFolders);

    GetSharedFolderResApp getSharedFolders(String userId, Pageable pageable, String name,Boolean showFolders);

    Boolean haveAccessRead(Folder folder, String userId);
    void deleteById(Long id,String userId);

    Page<TypeShareAccessRes> getAllPermission(long folderId, String userId, Pageable page);

    TypeShareAccessRes createOrUpdatePermission(long folderId, String userId, GranteeType granteeType, String granteeId, FolderPermissionReq permissionReq);

    void deletePermission(long folderId, String userId, String granteeId, boolean sub);
}
