package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {
    @Query("""
        SELECT dv
        FROM DocumentVersion dv
        WHERE dv.documentId IN :docIds
          AND dv.versionNumber = (
              SELECT MAX(dv2.versionNumber)
              FROM DocumentVersion dv2
              WHERE dv2.documentId = dv.documentId
          )
    """)
    List<DocumentVersion> findLatestVersionsByDocumentIds(@Param("docIds") List<Long> docIds);

    Optional<DocumentVersion> findFirstByDocumentIdAndDocumentDeletedAtIsNullOrderByVersionNumberDesc(Long objectId);

}
