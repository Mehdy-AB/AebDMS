package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.Document;
import com.Aeb.AebDMS.app.documents.model.DocumentPermission;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.model.FolderPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Add custom query methods here
    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DocumentClosure d
        WHERE d.documentId = :documentId
       \s""")
    boolean isPublic(
            @Param("documentId") Long documentId
    );

    @Query(value = """
        SELECT f.name
        FROM FilingCategory f
            join CategoryMetadataDefinition cmd on f.id = cmd.categoryId
                    join Metadata m on m.keyId = cmd.id
        WHERE m.documentId = :documentId
        """)
    List<String> getDocumentCategories(@Param("documentId") Long documentId);

    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DocumentClosure d
        JOIN DocumentPermission p ON d.permissionId = p.id
        WHERE d.documentId = :docId
          AND p.granteeId IN :granteeId
        """)
    boolean existsByDocumentIdAndPermissionReadAndUserId(
            @Param("docId") Long docId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DocumentClosure d
        JOIN DocumentPermission p ON d.permissionId = p.id
        WHERE d.documentId = :docId
        AND p.canEdit = true
          AND p.granteeId IN :granteeId
        """)
    boolean existsByDocumentIdAndPermissionEditAndUserId(
            @Param("docId") Long docId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DocumentClosure d
        JOIN DocumentPermission p ON d.permissionId = p.id
        WHERE d.documentId = :docId
        AND p.canManagePermissions = true
          AND p.granteeId IN :granteeId
        """)
    boolean existsByDocumentIdAndPermissionManagePermissionsAndUserId(
            @Param("docId") Long docId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DocumentClosure d
        JOIN DocumentPermission p ON d.permissionId = p.id
        WHERE d.documentId = :docId
        AND p.canDelete = true
          AND p.granteeId IN :granteeId
        """)
    boolean existsByDocumentIdAndPermissionDeleteAndUserId(
            @Param("docId") Long docId,
            @Param("granteeId") List<String> granteeId
    );

    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM Document d
        WHERE d.folderId = :folderId
        AND d.name = :docName
        """)
    boolean existsByFolderIdAndDocName(
            @Param("folderId") Long folderId,
            @Param("docName") String docName
    );

    @Query("""
    SELECT dv
    FROM DocumentVersion dv
    JOIN Document d ON dv.documentId = d.id
    LEFT JOIN DocumentClosure dc ON dc.documentId = d.id
    LEFT JOIN DocumentPermission p ON dc.permissionId = p.id
    WHERE d.folderId = :folderId
      AND dv.versionNumber = (
          SELECT MAX(dv2.versionNumber)
          FROM DocumentVersion dv2
          WHERE dv2.documentId = d.id
          AND  dv2.document.deletedAt IS NULL
      )
    """)
    Page<DocumentVersion> getLatestDocumentVersionsByFolderId(
            @Param("folderId") Long folderId,
            Pageable pageable
    );



    @Modifying
    @Query("UPDATE Document d SET d.deletedAt = :deletedAt WHERE d.folder.id IN :folderId")
    void updateDocumentsDeletedAt(@Param("deletedAt") Instant deletedAt, @Param("folderId") List<Long> folderId);

    @Query("""
    SELECT dp FROM DocumentClosure dc
    JOIN DocumentPermission dp ON dc.permissionId = dp.id
    WHERE
        dc.documentId = :documentId
    """)
    Page<DocumentPermission> getPermissionsByDocumentId(@Param("documentId") Long documentId, Pageable pageable);

    @Query("""
        SELECT DISTINCT fp
        FROM DocumentPermission fp
        JOIN DocumentClosure fc ON fc.permissionId = fp.id
        WHERE fc.documentId = :documentId
          AND fp.granteeId = :granteeId
    """)
    Optional<DocumentPermission> getPermissionByGranteeIdAndDocumentId(
            @Param("documentId") Long documentId,
            @Param("granteeId") String granteeId
    );

    @Query("""
    SELECT dv
    FROM DocumentVersion dv
    JOIN Document d ON dv.documentId = d.id
    WHERE d.id IN (
        SELECT dc.documentId
        FROM DocumentClosure dc
        JOIN DocumentPermission p ON dc.permissionId = p.id
        WHERE p.granteeId = :userId
          AND p.canView = true
    )
    AND d.folderId NOT IN (
        SELECT fc2.folderId
        FROM FolderClosure fc2
        JOIN FolderPermission p2 ON fc2.permissionId = p2.id
        WHERE p2.granteeId = :userId
          AND p2.canView = true
    )
    AND dv.versionNumber = (
        SELECT MAX(dv2.versionNumber)
        FROM DocumentVersion dv2
        WHERE dv2.documentId = d.id
          AND dv2.document.deletedAt IS NULL
    )
    """)
    Page<DocumentVersion> findLatestSharedRootDocuments(
            @Param("userId") String userId,
            Pageable pageable
    );

    @Query("""
    SELECT dv
    FROM DocumentVersion dv
    JOIN Document d ON dv.documentId = d.id
    LEFT JOIN DocumentClosure dc ON dc.documentId = d.id
    LEFT JOIN DocumentPermission p ON dc.permissionId = p.id
    WHERE d.folderId = :folderId
      AND dv.versionNumber = (
          SELECT MAX(dv2.versionNumber)
          FROM DocumentVersion dv2
          WHERE dv2.documentId = d.id
          AND  dv2.document.deletedAt IS NULL
      )
      AND LOWER(d.name) LIKE LOWER(CONCAT(:name, '%'))
    """)
    Page<DocumentVersion> getLatestDocumentVersionsByFolderIdByName(
            @Param("folderId") Long folderId,
            @Param("name") String name,
            Pageable pageable
    );

    @Query("""
    SELECT dv
    FROM DocumentVersion dv
    JOIN Document d ON dv.documentId = d.id
    WHERE d.id IN (
        SELECT dc.documentId
        FROM DocumentClosure dc
        JOIN DocumentPermission p ON dc.permissionId = p.id
        WHERE p.granteeId = :userId
          AND p.canView = true
    )
    AND d.folderId NOT IN (
        SELECT fc2.folderId
        FROM FolderClosure fc2
        JOIN FolderPermission p2 ON fc2.permissionId = p2.id
        WHERE p2.granteeId = :userId
          AND p2.canView = true
    )
    AND dv.versionNumber = (
        SELECT MAX(dv2.versionNumber)
        FROM DocumentVersion dv2
        WHERE dv2.documentId = d.id
          AND dv2.document.deletedAt IS NULL
    )
    AND LOWER(d.name) LIKE LOWER(CONCAT(:name, '%'))
    """)
    Page<DocumentVersion> findLatestSharedRootDocumentsByName(
            @Param("userId") String userId,
            @Param("name") String name,
            Pageable pageable
    );
}
