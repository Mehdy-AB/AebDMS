package com.Aeb.AebDMS.app.shares.service.impl;

import com.Aeb.AebDMS.app.shares.model.Share;
import com.Aeb.AebDMS.app.shares.service.IShareService;
import com.Aeb.AebDMS.app.shares.repository.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements IShareService {

    private final ShareRepository shareRepository;

    @Override
    public Share saveShare(Share share) {
        return shareRepository.save(share);
    }

    @Override
    public List<Share> findAll() {
        return shareRepository.findAll();
    }

    @Override
    public Page<Share> findAll(Pageable pageable) {
        return shareRepository.findAll(pageable);
    }

    @Override
    public Optional<Share> findById(Long id) {
        return shareRepository.findById(id);
    }

    @Override
    public Share updateShare(Share share) {
        return null;
      //  return shareRepository.save(share);
    }

    @Override
    public void deleteById(Long id) {

        //shareRepository.deleteById(id);
    }
}
