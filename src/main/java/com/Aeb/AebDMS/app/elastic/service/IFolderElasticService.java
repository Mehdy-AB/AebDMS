package com.Aeb.AebDMS.app.elastic.service;

import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IFolderElasticService {
    
    FolderElastic saveFolderDocument(FolderElastic folderElastic);
    
    List<FolderElastic> findAll();
    
    Page<FolderElastic> findAll(Pageable pageable);
    
    Optional<FolderElastic> findById(String id);
    
    List<FolderElastic> searchByName(String name);
    
    FolderElastic updateFolderDocument(FolderElastic folderElastic);
    
    void deleteById(String id);

    void deleteByIds(Set<Long> toDeleteEsFolderIds);

    void removeGrantedIdsFromDocs(Set<String> userIdSet, Set<Long> toRemoveGrantFromFolders);
}
