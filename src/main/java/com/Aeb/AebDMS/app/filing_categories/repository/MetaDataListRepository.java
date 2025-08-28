package com.Aeb.AebDMS.app.filing_categories.repository;

import com.Aeb.AebDMS.app.filing_categories.model.CategoryMetadataDefinition;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDataListRepository extends JpaRepository<ListMetaData, Long> {
    @Query("""
    SELECT f FROM ListMetaData f
    WHERE LOWER(f.name) = LOWER(concat(:name,"%"))
    """)
    Page<ListMetaData> findAllByName(@Param("name") String name, Pageable pageable);
}
