package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.model.Alias;
import com.Aeb.AebDMS.app.user.service.IAliasService;
import com.Aeb.AebDMS.app.user.repository.AliasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AliasServiceImpl implements IAliasService {

    private final AliasRepository aliasRepository;

    @Override
    public Alias saveAlias(Alias alias) {
        return aliasRepository.save(alias);
    }

    @Override
    public List<Alias> findAll() {
        return aliasRepository.findAll();
    }

    @Override
    public Page<Alias> findAll(Pageable pageable) {
        return aliasRepository.findAll(pageable);
    }

    @Override
    public Optional<Alias> findById(Long id) {
        return aliasRepository.findById(id);
    }

    @Override
    public Alias updateAlias(Alias alias) {
        return aliasRepository.save(alias);
    }

    @Override
    public void deleteById(Long id) {
        aliasRepository.deleteById(id);
    }
}
