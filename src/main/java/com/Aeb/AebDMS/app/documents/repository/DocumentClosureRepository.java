package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.DocumentClosure;
import com.Aeb.AebDMS.app.documents.model.DocumentPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentClosureRepository extends JpaRepository<DocumentClosure, DocumentPermissionId> {
    @Query("SELECT COUNT(fc) FROM DocumentClosure fc WHERE fc.permissionId = :permissionId")
    long countByPermissionId(@Param("permissionId") Long permissionId);
}
