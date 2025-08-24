package com.Aeb.AebDMS.app.folders.repository;

import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.model.FolderClosure;
import com.Aeb.AebDMS.app.folders.model.FolderPermission;
import com.Aeb.AebDMS.app.folders.model.FolderPermissionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FolderClosureRepository extends JpaRepository<FolderClosure, FolderPermissionId> {

    @Query("SELECT COUNT(fc) FROM FolderClosure fc WHERE fc.permissionId = :permissionId")
    long countByPermissionId(@Param("permissionId") Long permissionId);

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canCreateSubFolders = true
          AND p.granteeId IN :granteeId
        """)
    boolean existsByFolderIdAndPermissionWriteAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canUpload = true
          AND p.granteeId IN :granteeId
        """)
    boolean existsByFolderIdAndPermissionUploadAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canView = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionReadAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canEdit = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionEditAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canDelete = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionDeleteAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canManagePermissions = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionManagePermissionsAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
      SELECT DISTINCT f
      FROM Folder f
      LEFT JOIN FolderClosure fc ON fc.folderId = f.id
      LEFT JOIN FolderPermission p ON fc.permissionId = p.id
      WHERE f.parentId = :parentId
        AND (f.createdBy = :userId OR p.granteeId IN :granteeIds)
    """)
    Page<Folder> getSharedFolders(
            @Param("parentId") Long parentId,
            @Param("userId") String userId,
            @Param("granteeIds") List<String> granteeIds,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT fp
        FROM FolderPermission fp
        JOIN FolderClosure fc ON fc.permissionId = fp.id
        WHERE fc.folderId = :folderId
          AND fp.granteeId = :granteeId
    """)
    Optional<FolderPermission> getPermissionByGranteeIdAndFolderId(
            @Param("folderId") Long folderId,
            @Param("granteeId") String granteeId
    );

    @Query("""
        DELETE FROM FolderClosure f
        WHERE f.folderId IN :folderIds
          AND f.permissionId = :permissionId
    """)
    void deleteAllByFolderIdsAndPermissionId(@Param("folderIds") Set<Long> folderIds,
                                             @Param("permissionId") Long permissionId);

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canEditDoc = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionEditDocAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canDeleteDoc = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionDeleteDocAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM FolderClosure f
        JOIN FolderPermission p ON f.permissionId = p.id
        WHERE f.folderId = :folderId
          AND p.canManagePermissionsDoc = true
          AND p.granteeId IN :userId
        """)
    boolean existsByFolderIdAndPermissionManagePermissionsDocAndUserId(
            @Param("folderId") Long folderId,
            @Param("granteeId") List<String> granteeId
    );


}
