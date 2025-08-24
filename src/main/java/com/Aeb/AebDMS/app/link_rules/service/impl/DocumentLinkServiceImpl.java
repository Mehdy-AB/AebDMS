package com.Aeb.AebDMS.app.link_rules.service.impl;

import com.Aeb.AebDMS.app.link_rules.model.DocumentLink;
import com.Aeb.AebDMS.app.link_rules.service.IDocumentLinkService;
import com.Aeb.AebDMS.app.link_rules.repository.DocumentLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentLinkServiceImpl implements IDocumentLinkService {

    private final DocumentLinkRepository documentlinkRepository;

    @Override
    public DocumentLink saveDocumentLink(DocumentLink documentlink) {
        return documentlinkRepository.save(documentlink);
    }

    @Override
    public List<DocumentLink> findAll() {
        return documentlinkRepository.findAll();
    }

    @Override
    public Page<DocumentLink> findAll(Pageable pageable) {
        return documentlinkRepository.findAll(pageable);
    }

    @Override
    public Optional<DocumentLink> findById(Long id) {
        return documentlinkRepository.findById(id);
    }

    @Override
    public DocumentLink updateDocumentLink(DocumentLink documentlink) {
        return documentlinkRepository.save(documentlink);
    }

    @Override
    public void deleteById(Long id) {
        documentlinkRepository.deleteById(id);
    }
}
