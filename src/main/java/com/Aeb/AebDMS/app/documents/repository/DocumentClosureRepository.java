package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.DocumentClosure;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentClosureRepository extends JpaRepository<DocumentClosure, Long> {
    @Query("SELECT COUNT(fc) FROM DocumentClosure fc WHERE fc.permissionId = :permissionId")
    long countByPermissionId(@Param("permissionId") Long permissionId);
}
