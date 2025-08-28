package com.Aeb.AebDMS.app.filing_categories.repository;

import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilingCategoryRepository extends JpaRepository<FilingCategory, Long> {
    @Query("""
    SELECT f FROM FilingCategory f
    WHERE LOWER(f.name) = LOWER(concat(:name,"%"))
    """)
    Page<FilingCategory> findAllByName(@Param("name") String name, Pageable pageable);
}
