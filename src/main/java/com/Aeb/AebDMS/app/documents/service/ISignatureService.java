package com.Aeb.AebDMS.app.documents.service;

import com.Aeb.AebDMS.app.documents.model.Signature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISignatureService {
    
    Signature saveSignature(Signature signature);
    
    List<Signature> findAll();
    
    Page<Signature> findAll(Pageable pageable);
    
    Optional<Signature> findById(Long id);
    
    Signature updateSignature(Signature signature);
    
    void deleteById(Long id);
}
