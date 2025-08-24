package com.Aeb.AebDMS.app.documents.service.impl;

import com.Aeb.AebDMS.app.documents.model.Signature;
import com.Aeb.AebDMS.app.documents.service.ISignatureService;
import com.Aeb.AebDMS.app.documents.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements ISignatureService {

    private final SignatureRepository signatureRepository;

    @Override
    public Signature saveSignature(Signature signature) {
        return signatureRepository.save(signature);
    }

    @Override
    public List<Signature> findAll() {
        return signatureRepository.findAll();
    }

    @Override
    public Page<Signature> findAll(Pageable pageable) {
        return signatureRepository.findAll(pageable);
    }

    @Override
    public Optional<Signature> findById(Long id) {
        return signatureRepository.findById(id);
    }

    @Override
    public Signature updateSignature(Signature signature) {
        return signatureRepository.save(signature);
    }

    @Override
    public void deleteById(Long id) {
        signatureRepository.deleteById(id);
    }
}
