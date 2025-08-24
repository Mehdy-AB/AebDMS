package com.Aeb.AebDMS.app.filing_categories.service;

import com.Aeb.AebDMS.app.filing_categories.model.Metadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMetadataService {
    
    Metadata saveMetadata(Metadata metadata);
    
    List<Metadata> findAll();
    
    Page<Metadata> findAll(Pageable pageable);
    
    Optional<Metadata> findById(Long id);
    
    Metadata updateMetadata(Metadata metadata);
    
    void deleteById(Long id);
}
