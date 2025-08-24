package com.Aeb.AebDMS.app.shares.service;

import com.Aeb.AebDMS.app.shares.model.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IShareService {
    
    Share saveShare(Share share);
    
    List<Share> findAll();
    
    Page<Share> findAll(Pageable pageable);
    
    Optional<Share> findById(Long id);
    
    Share updateShare(Share share);
    
    void deleteById(Long id);
}
