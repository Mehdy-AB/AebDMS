package com.Aeb.AebDMS.app.documents.repository;

import com.Aeb.AebDMS.app.documents.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {
    // Add custom query methods here
}
