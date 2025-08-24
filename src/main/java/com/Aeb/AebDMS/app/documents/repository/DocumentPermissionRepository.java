package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.Document;
import com.Aeb.AebDMS.app.documents.model.DocumentPermission;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
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
public interface DocumentPermissionRepository extends JpaRepository<DocumentPermission, Long> {

    @Query("""
        DELETE FROM DocumentClosure dc
        WHERE dc.documentId = :documentId
        AND dc.permissionId = :permissionId
    """)
    void deleteByDocumentIdAndPermissionId(Long documentId, Long permissionId);

}
