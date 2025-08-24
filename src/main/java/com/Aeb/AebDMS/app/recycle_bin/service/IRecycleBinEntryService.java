package com.Aeb.AebDMS.app.recycle_bin.service;

import com.Aeb.AebDMS.app.recycle_bin.model.RecycleBinEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IRecycleBinEntryService {
    
    RecycleBinEntry saveRecycleBinEntry(RecycleBinEntry recyclebinentry);
    
    List<RecycleBinEntry> findAll();
    
    Page<RecycleBinEntry> findAll(Pageable pageable);
    
    Optional<RecycleBinEntry> findById(Long id);
    
    RecycleBinEntry updateRecycleBinEntry(RecycleBinEntry recyclebinentry);
    
    void deleteById(Long id);
}
