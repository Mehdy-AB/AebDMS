package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.model.Alias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IAliasService {
    
    Alias saveAlias(Alias alias);
    
    List<Alias> findAll();
    
    Page<Alias> findAll(Pageable pageable);
    
    Optional<Alias> findById(Long id);
    
    Alias updateAlias(Alias alias);
    
    void deleteById(Long id);
}
