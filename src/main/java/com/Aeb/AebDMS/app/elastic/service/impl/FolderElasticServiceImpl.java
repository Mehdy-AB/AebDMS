package com.Aeb.AebDMS.app.elastic.service.impl;

import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import com.Aeb.AebDMS.app.elastic.service.IFolderElasticService;
import com.Aeb.AebDMS.app.elastic.repository.FolderDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class FolderElasticServiceImpl implements IFolderElasticService {

    private final FolderDocumentRepository folderdocumentRepository;


    @Override
    public FolderElastic saveFolderDocument(FolderElastic folderElastic) {
        return folderdocumentRepository.save(folderElastic);
    }

    @Override
    public List<FolderElastic> findAll() {
        return (List<FolderElastic>) folderdocumentRepository.findAll();
    }

    @Override
    public Page<FolderElastic> findAll(Pageable pageable) {
        return folderdocumentRepository.findAll(pageable);
    }

    @Override
    public Optional<FolderElastic> findById(Long id) {
        return folderdocumentRepository.findById(id);
    }

    @Override
    public List<FolderElastic> searchByName(String name) {
        // Implement custom search logic using Elasticsearch
        // Example: return folderdocumentRepository.findByNameContaining(name);
        return null;
    }

    @Override
    public FolderElastic updateFolderDocument(FolderElastic folderElastic) {
        return folderdocumentRepository.save(folderElastic);
    }

    @Override
    public void deleteById(Long id) {
        folderdocumentRepository.deleteById(id);
    }


    @Override
    public void deleteByIds(Set<Long> toDeleteEsFolderIds) {
        folderdocumentRepository.deleteAllById(toDeleteEsFolderIds.stream().toList());
    }

    @Override
    public void removeGrantedIdsFromDocs(Set<String> userIdSet, Set<Long> toRemoveGrantFromFolders) {
        toRemoveGrantFromFolders.forEach(id -> {
            folderdocumentRepository.findById(id).ifPresent(doc -> {
                doc.setGrantedIds(doc.getGrantedIds().stream()
                        .filter(g -> !userIdSet.contains(g))
                        .toList());
                if (doc.getGrantedIds().isEmpty()) folderdocumentRepository.delete(doc);
                else folderdocumentRepository.save(doc);
            });
        });
    }

}
