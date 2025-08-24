package com.Aeb.AebDMS.app.filing_categories.service.impl;

import com.Aeb.AebDMS.app.filing_categories.model.Metadata;
import com.Aeb.AebDMS.app.filing_categories.service.IMetadataService;
import com.Aeb.AebDMS.app.filing_categories.repository.MetadataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements IMetadataService {

    private final MetadataRepository metadataRepository;

    @Override
    public Metadata saveMetadata(Metadata metadata) {
        return metadataRepository.save(metadata);
    }

    @Override
    public List<Metadata> findAll() {
        return metadataRepository.findAll();
    }

    @Override
    public Page<Metadata> findAll(Pageable pageable) {
        return metadataRepository.findAll(pageable);
    }

    @Override
    public Optional<Metadata> findById(Long id) {
        return metadataRepository.findById(id);
    }

    @Override
    public Metadata updateMetadata(Metadata metadata) {
        return metadataRepository.save(metadata);
    }

    @Override
    public void deleteById(Long id) {
        metadataRepository.deleteById(id);
    }
}
