package com.Aeb.AebDMS.app.folders.repository;

import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.model.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long>, JpaSpecificationExecutor<Folder> {
    // Add custom query methods here

    Optional<Folder> findByIdAndDeletedAtIsNull(Long aLong);

    @Query(value = """
        SELECT
            (SELECT COUNT(*) FROM folders sf WHERE sf.parent_id = :folderId) +
            (SELECT COUNT(*) FROM documents d WHERE d.folder_id = :folderId)
    """, nativeQuery = true)
    Long countSubFoldersAndDocuments(@Param("folderId") Long folderId);

    @Query(value = """
      SELECT f.path FROM Folder f WHERE f.id = :folderId
    """)
    String getFolderPath(@Param("folderId") Long folderId);

    @Query("""
    SELECT f.id, f.path FROM Folder f WHERE f.id IN :folderIds
    """)
    List<Object[]> getFoldersIdAndPath(@Param("folderIds") Set<Long> folderIds);


//    @Query(value = """
//      SELECT f,(SELECT COUNT(*) FROM folders sf WHERE sf.parent_id = f.id)
//               +
//               (SELECT COUNT(*) FROM documents d WHERE d.folder_id = f.id) AS totalCount
//          FROM Folder f WHERE
//          f.parentId IS NULL
//          AND f.createdBy = :userId
//   \s""", nativeQuery = true)
//    Page<Folder> getMyRepo(@Param("userId") String userId, Pageable pageable);

    @Query(value = """
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        )
        FROM Folder f
        WHERE f.parentId = :folderId
        And f.deletedAt IS NULL
    """)
    Page<FolderWithCountDto> getFolderContent( @Param("folderId") Long folderId, Pageable pageable);


    // in FolderRepository
    @Query(value = """
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        )
        FROM Folder f
        WHERE f.parent IS NULL
        And f.ownedBy = :userId
        And f.deletedAt IS NULL
    """)
    Page<FolderWithCountDto> getMyRepo(@Param("userId") String userId, Pageable pageable);

    @Query("""
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        ) FROM Folder f
        WHERE f.id IN (
            SELECT fc.folderId FROM FolderClosure fc
            JOIN FolderPermission p
            WHERE p.granteeId = :userId
        )
        AND f.parent.id NOT IN (
            SELECT fc2.folderId FROM FolderClosure fc2
            JOIN FolderPermission p2
            WHERE p2.granteeId = :userId
            AND p2.canView = true
        )
        """)
    Page<FolderWithCountDto> findSharedRootFolders(@Param("userId") String userId, Pageable pageable);

    //whene search with name
    @Query(value = """
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        )
        FROM Folder f
        WHERE f.parentId = :folderId
        And f.deletedAt IS NULL
        AND LOWER(f.name) LIKE LOWER(CONCAT(:name, '%'))
    """)
    Page<FolderWithCountDto> getFolderContentByName( @Param("folderId") Long folderId,@Param("name") String name, Pageable pageable);
    // in FolderRepository
    @Query(value = """
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        )
        FROM Folder f
        WHERE f.parent IS NULL
        And f.ownedBy = :userId
        And f.deletedAt IS NULL
        AND LOWER(f.name) LIKE LOWER(CONCAT(:name, '%'))
    """)
    Page<FolderWithCountDto> getMyRepoByName(@Param("userId") String userId,@Param("name") String name, Pageable pageable);

    @Query("""
        SELECT new com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto(
            f,
            (SELECT COUNT(sf) FROM Folder sf WHERE sf.parent.id = f.id),
            (SELECT COUNT(d) FROM Document d WHERE d.folder.id = f.id)
        ) FROM Folder f
        WHERE f.id IN (
            SELECT fc.folderId FROM FolderClosure fc
            JOIN FolderPermission p
            WHERE p.granteeId = :userId
        )
        AND f.parent.id NOT IN (
            SELECT fc2.folderId FROM FolderClosure fc2
            JOIN FolderPermission p2
            WHERE p2.granteeId = :userId
            AND p2.canView = true
            AND LOWER(f.name) LIKE LOWER(CONCAT(:name, '%'))
        )
        """)
    Page<FolderWithCountDto> findSharedRootFoldersByName(@Param("userId") String userId,@Param("name") String name, Pageable pageable);

    @Modifying
    @Query("UPDATE Folder f SET f.deletedAt = :deletedAt WHERE f.parent.id IN :parentId")
    void updateSubfoldersDeletedAt(@Param("deletedAt") Instant deletedAt, @Param("parentId") List<Long> parentId);

    @Modifying
    @Query("UPDATE Folder f SET f.isPublic = :isPublic WHERE f.id IN :foldersId")
    void updateFoldersIsPublic(@Param("isPublic") boolean isPublic, @Param("foldersId") List<Long> foldersId);


    Optional<Folder> findByPathAndDeletedAtIsNull(String path);
}
