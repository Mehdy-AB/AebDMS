package com.Aeb.AebDMS.app.shares.service;

import com.Aeb.AebDMS.app.shares.model.ShareAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IShareAccessService {
    
    ShareAccess saveShareAccess(ShareAccess shareaccess);
    
    List<ShareAccess> findAll();
    
    Page<ShareAccess> findAll(Pageable pageable);
    
    //Optional<ShareAccess> findById(Long id);
    
    ShareAccess updateShareAccess(ShareAccess shareaccess);
    
    void deleteById(Long id);
}
