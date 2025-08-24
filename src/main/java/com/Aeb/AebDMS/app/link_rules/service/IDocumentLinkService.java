package com.Aeb.AebDMS.app.link_rules.service;

import com.Aeb.AebDMS.app.link_rules.model.DocumentLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDocumentLinkService {
    
    DocumentLink saveDocumentLink(DocumentLink documentlink);
    
    List<DocumentLink> findAll();
    
    Page<DocumentLink> findAll(Pageable pageable);
    
    Optional<DocumentLink> findById(Long id);
    
    DocumentLink updateDocumentLink(DocumentLink documentlink);
    
    void deleteById(Long id);
}
