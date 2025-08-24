package com.Aeb.AebDMS.app.elastic.service;

import com.Aeb.AebDMS.app.documents.dto.MetadataTransferee;
import com.Aeb.AebDMS.app.elastic.model.MetadataDocumentElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMetadataDocumentServiceElastic {

    void saveAllMetadataDocument(List <MetadataTransferee> metadataTransferees);
    
    List<MetadataDocumentElastic> findAll();
    
    Page<MetadataDocumentElastic> findAll(Pageable pageable);
    
    Optional<MetadataDocumentElastic> findById(String id);
    
    List<MetadataDocumentElastic> searchByName(String name);
    
    MetadataDocumentElastic updateMetadataDocument(MetadataDocumentElastic metadataDocument);
    
    void deleteById(String id);
}
