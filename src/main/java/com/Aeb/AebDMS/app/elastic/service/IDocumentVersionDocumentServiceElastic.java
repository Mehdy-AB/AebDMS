package com.Aeb.AebDMS.app.elastic.service;

import com.Aeb.AebDMS.app.elastic.model.DocumentVersionDocumentElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDocumentVersionDocumentServiceElastic {
    
    void saveDocumentVersionDocument(DocumentVersionDocumentElastic documentVersionDocument);
    
    List<DocumentVersionDocumentElastic> findAll();
    
    Page<DocumentVersionDocumentElastic> findAll(Pageable pageable);
    
    Optional<DocumentVersionDocumentElastic> findById(String id);
    
    List<DocumentVersionDocumentElastic> searchByName(String name);
    
    DocumentVersionDocumentElastic updateDocumentVersionDocument(DocumentVersionDocumentElastic documentVersionDocument);
    
    void deleteById(String id);

    void deleteByIds(Set<Long> toDeleteEsDocIds);

    void removeGrantedIdsFromDocs(Set<String> userIdSet, Set<Long> toRemoveGrantFromDocs);
}
