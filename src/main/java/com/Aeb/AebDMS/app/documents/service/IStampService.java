package com.Aeb.AebDMS.app.documents.service;

import com.Aeb.AebDMS.app.documents.model.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IStampService {
    
    Stamp saveStamp(Stamp stamp);
    
    List<Stamp> findAll();
    
    Page<Stamp> findAll(Pageable pageable);
    
    Optional<Stamp> findById(Long id);
    
    Stamp updateStamp(Stamp stamp);
    
    void deleteById(Long id);
}
