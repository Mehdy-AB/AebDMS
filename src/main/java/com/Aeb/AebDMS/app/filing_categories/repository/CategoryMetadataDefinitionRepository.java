package com.Aeb.AebDMS.app.filing_categories.repository;

import com.Aeb.AebDMS.app.filing_categories.model.CategoryMetadataDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataDefinitionRepository extends JpaRepository<CategoryMetadataDefinition, Long> {
    // Add custom query methods here
}
