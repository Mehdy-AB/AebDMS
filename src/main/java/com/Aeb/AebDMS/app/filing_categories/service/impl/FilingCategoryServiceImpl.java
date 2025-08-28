package com.Aeb.AebDMS.app.filing_categories.service.impl;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import com.Aeb.AebDMS.app.filing_categories.service.IFilingCategoryService;
import com.Aeb.AebDMS.app.filing_categories.repository.FilingCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FilingCategoryServiceImpl implements IFilingCategoryService {

    private final FilingCategoryRepository filingcategoryRepository;

    @Override
    public FilingCategory saveFilingCategory(FilingCategory filingcategory) {
        return filingcategoryRepository.save(filingcategory);
    }

    @Override
    public Page<FilingCategory> findAll(String name,Pageable pageable) {
        if(name == null || name.isEmpty())
            return filingcategoryRepository.findAll(pageable);
        return filingcategoryRepository.findAllByName(name,pageable);
    }

    @Override
    public FilingCategory findById(Long id) {
        return filingcategoryRepository.findById(id).orElseThrow(() -> new BaseException("FilingCategory not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public FilingCategory updateFilingCategory(FilingCategory updatedCategory) {
        FilingCategory existing = filingcategoryRepository.findById(updatedCategory.getId())
                .orElseThrow(() -> new BaseException("FilingCategory not found", HttpStatus.NOT_FOUND));

        // Clear old metadata
        existing.getMetadataDefinitions().clear();

        // Replace with new metadata (make sure to set parent)
        if (updatedCategory.getMetadataDefinitions() != null) {
            updatedCategory.getMetadataDefinitions().forEach(meta -> meta.setCategory(existing));
            existing.getMetadataDefinitions().addAll(updatedCategory.getMetadataDefinitions());
        }

        // Update other fields
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        // Add any other fields as needed
        try {
            return filingcategoryRepository.save(existing);
        }catch (Exception e) {
            throw new BaseException("FilingCategory could not be saved", HttpStatus.BAD_REQUEST);
        }

    }


    @Override
    public void deleteById(Long id) {
        filingcategoryRepository.deleteById(id);
    }
}
