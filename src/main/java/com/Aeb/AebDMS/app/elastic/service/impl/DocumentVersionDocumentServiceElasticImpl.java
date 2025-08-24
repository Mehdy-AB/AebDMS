package com.Aeb.AebDMS.app.elastic.service.impl;

import com.Aeb.AebDMS.app.elastic.model.DocumentVersionDocumentElastic;
import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import com.Aeb.AebDMS.app.elastic.service.IDocumentVersionDocumentServiceElastic;
import com.Aeb.AebDMS.app.elastic.repository.DocumentVersionDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class DocumentVersionDocumentServiceElasticImpl implements IDocumentVersionDocumentServiceElastic {

    private final DocumentVersionDocumentRepository documentversiondocumentRepository;

    @Override
    public void saveDocumentVersionDocument(DocumentVersionDocumentElastic documentVersionDocument) {
        documentversiondocumentRepository.save(documentVersionDocument);
    }

    @Override
    public List<DocumentVersionDocumentElastic> findAll() {
        return (List<DocumentVersionDocumentElastic>) documentversiondocumentRepository.findAll();
    }

    @Override
    public Page<DocumentVersionDocumentElastic> findAll(Pageable pageable) {
        return documentversiondocumentRepository.findAll(pageable);
    }

    @Override
    public Optional<DocumentVersionDocumentElastic> findById(String id) {
        return documentversiondocumentRepository.findById(id);
    }

    @Override
    public List<DocumentVersionDocumentElastic> searchByName(String name) {
        // Implement custom search logic using Elasticsearch
        // Example: return documentversiondocumentRepository.findByNameContaining(name);
        return null;
    }

    @Override
    public DocumentVersionDocumentElastic updateDocumentVersionDocument(DocumentVersionDocumentElastic documentversiondocument) {
        return documentversiondocumentRepository.save(documentversiondocument);
    }

    @Override
    public void deleteById(String id) {
        documentversiondocumentRepository.deleteById(id);
    }



    @Override
    public void deleteByIds(Set<Long> toDeleteEsDocIds) {
        documentversiondocumentRepository.deleteAllById(toDeleteEsDocIds.stream().map(Object::toString).toList());
    }

    @Override
    public void removeGrantedIdsFromDocs(Set<String> userIdSet, Set<Long> toRemoveGrantFromDocs) {
        toRemoveGrantFromDocs.forEach(id -> {
            documentversiondocumentRepository.findById(id.toString()).ifPresent(doc -> {
                doc.setGrantedIds(doc.getGrantedIds().stream()
                        .filter(g -> !userIdSet.contains(g))
                        .toList());
                if (doc.getGrantedIds().isEmpty()) documentversiondocumentRepository.delete(doc);
                else documentversiondocumentRepository.save(doc);
            });
        });
    }
}
