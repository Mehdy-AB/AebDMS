package com.Aeb.AebDMS.app.shares.service.impl;

import com.Aeb.AebDMS.app.shares.model.ShareAccess;
import com.Aeb.AebDMS.app.shares.service.IShareAccessService;
import com.Aeb.AebDMS.app.shares.repository.ShareAccessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareAccessServiceImpl implements IShareAccessService {

    private final ShareAccessRepository shareaccessRepository;

    @Override
    public ShareAccess saveShareAccess(ShareAccess shareaccess) {
        return shareaccessRepository.save(shareaccess);
    }

    @Override
    public List<ShareAccess> findAll() {
        return shareaccessRepository.findAll();
    }

    @Override
    public Page<ShareAccess> findAll(Pageable pageable) {
        return shareaccessRepository.findAll(pageable);
    }

//    @Override
//    public Optional<ShareAccess> findById(Long id) {
//        //return shareaccessRepository.findById(id);
//    }

    @Override
    public ShareAccess updateShareAccess(ShareAccess shareaccess) {
        return shareaccessRepository.save(shareaccess);
    }

    @Override
    public void deleteById(Long id) {
        //shareaccessRepository.deleteById(id);
    }
}
