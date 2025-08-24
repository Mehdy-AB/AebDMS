package com.Aeb.AebDMS.app.filing_categories.repository;

import com.Aeb.AebDMS.app.filing_categories.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, Long> {
    // Add custom query methods here
}
