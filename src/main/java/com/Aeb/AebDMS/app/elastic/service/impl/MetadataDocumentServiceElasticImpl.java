package com.Aeb.AebDMS.app.elastic.service.impl;

import com.Aeb.AebDMS.app.documents.dto.MetadataTransferee;
import com.Aeb.AebDMS.app.elastic.model.MetadataDocumentElastic;
import com.Aeb.AebDMS.app.elastic.repository.MetadataDocumentRepository;
import com.Aeb.AebDMS.app.elastic.service.IMetadataDocumentServiceElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class MetadataDocumentServiceElasticImpl implements IMetadataDocumentServiceElastic {

    private final MetadataDocumentRepository metadatadocumentRepository;

    @Override
    public void saveAllMetadataDocument(List <MetadataTransferee> metadataTransferees) {
        List <MetadataDocumentElastic> metadataDocument =  metadataTransferees.stream().map(metadataTransferee -> MetadataDocumentElastic
                .builder()
                        .metadataId(metadataTransferee.getMetadataId())
                        .categoryId(metadataTransferee.getCategoryId())
                        .value(metadataTransferee.getValue())
                        .categoryName(metadataTransferee.getCategoryName())
                        .key(metadataTransferee.getMetaDataName())
                .build())
                .collect(Collectors.toList());
         metadatadocumentRepository.saveAll(metadataDocument);
    }

    @Override
    public List<MetadataDocumentElastic> findAll() {
        return (List<MetadataDocumentElastic>) metadatadocumentRepository.findAll();
    }

    @Override
    public Page<MetadataDocumentElastic> findAll(Pageable pageable) {
        return metadatadocumentRepository.findAll(pageable);
    }

    @Override
    public Optional<MetadataDocumentElastic> findById(String id) {
        return metadatadocumentRepository.findById(id);
    }

    @Override
    public List<MetadataDocumentElastic> searchByName(String name) {
        // Implement custom search logic using Elasticsearch
        // Example: return metadatadocumentRepository.findByNameContaining(name);
        return null;
    }

    @Override
    public MetadataDocumentElastic updateMetadataDocument(MetadataDocumentElastic metadataDocument) {
        return metadatadocumentRepository.save(metadataDocument);
    }

    @Override
    public void deleteById(String id) {
        metadatadocumentRepository.deleteById(id);
    }


}
