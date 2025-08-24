package com.Aeb.AebDMS.app.documents.service.impl;

import com.Aeb.AebDMS.app.documents.model.Stamp;
import com.Aeb.AebDMS.app.documents.service.IStampService;
import com.Aeb.AebDMS.app.documents.repository.StampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StampServiceImpl implements IStampService {

    private final StampRepository stampRepository;

    @Override
    public Stamp saveStamp(Stamp stamp) {
        return stampRepository.save(stamp);
    }

    @Override
    public List<Stamp> findAll() {
        return stampRepository.findAll();
    }

    @Override
    public Page<Stamp> findAll(Pageable pageable) {
        return stampRepository.findAll(pageable);
    }

    @Override
    public Optional<Stamp> findById(Long id) {
        return stampRepository.findById(id);
    }

    @Override
    public Stamp updateStamp(Stamp stamp) {
        return stampRepository.save(stamp);
    }

    @Override
    public void deleteById(Long id) {
        stampRepository.deleteById(id);
    }
}
