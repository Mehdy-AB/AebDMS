package com.Aeb.AebDMS.app.recycle_bin.service.impl;

import com.Aeb.AebDMS.app.recycle_bin.model.RecycleBinEntry;
import com.Aeb.AebDMS.app.recycle_bin.service.IRecycleBinEntryService;
import com.Aeb.AebDMS.app.recycle_bin.repository.RecycleBinEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecycleBinEntryServiceImpl implements IRecycleBinEntryService {

    private final RecycleBinEntryRepository recyclebinentryRepository;

    @Override
    public RecycleBinEntry saveRecycleBinEntry(RecycleBinEntry recyclebinentry) {
        return recyclebinentryRepository.save(recyclebinentry);
    }

    @Override
    public List<RecycleBinEntry> findAll() {
        return recyclebinentryRepository.findAll();
    }

    @Override
    public Page<RecycleBinEntry> findAll(Pageable pageable) {
        return recyclebinentryRepository.findAll(pageable);
    }

    @Override
    public Optional<RecycleBinEntry> findById(Long id) {
        return recyclebinentryRepository.findById(id);
    }

    @Override
    public RecycleBinEntry updateRecycleBinEntry(RecycleBinEntry recyclebinentry) {
        return recyclebinentryRepository.save(recyclebinentry);
    }

    @Override
    public void deleteById(Long id) {
        recyclebinentryRepository.deleteById(id);
    }
}
