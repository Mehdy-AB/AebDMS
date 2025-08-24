package com.Aeb.AebDMS.app.filing_categories.repository;

import com.Aeb.AebDMS.app.filing_categories.model.CategoryMetadataDefinition;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDataListRepository extends JpaRepository<ListMetaData, Long> {
    // Add custom query methods here
}
