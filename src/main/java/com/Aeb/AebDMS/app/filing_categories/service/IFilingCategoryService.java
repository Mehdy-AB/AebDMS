package com.Aeb.AebDMS.app.filing_categories.service;

import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IFilingCategoryService {
    
    FilingCategory saveFilingCategory(FilingCategory filingcategory);

    Page<FilingCategory> findAll(String name,Pageable pageable);
    
    FilingCategory findById(Long id);
    
    FilingCategory updateFilingCategory(FilingCategory filingcategory);
    
    void deleteById(Long id);
}
