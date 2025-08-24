package com.Aeb.AebDMS.app.folders.repository;

import com.Aeb.AebDMS.app.folders.model.FolderPermission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;


@Repository
public interface FolderPermissionRepository extends JpaRepository<FolderPermission, Long> {
    // Add custom query methods here
    @Query("""
    SELECT fp FROM FolderClosure fc
    JOIN FolderPermission fp ON fc.permissionId = fp.id
    WHERE
        fc.folderId = :folderId
    """)
    Page<FolderPermission> getPermissionsByFolderId(@Param("folderId") Long folderId, Pageable pageable);

}
