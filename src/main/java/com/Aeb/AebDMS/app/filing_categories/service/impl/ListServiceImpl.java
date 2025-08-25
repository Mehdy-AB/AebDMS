package com.Aeb.AebDMS.app.filing_categories.service.impl;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import com.Aeb.AebDMS.app.filing_categories.model.Metadata;
import com.Aeb.AebDMS.app.filing_categories.repository.MetaDataListRepository;
import com.Aeb.AebDMS.app.filing_categories.repository.MetadataRepository;
import com.Aeb.AebDMS.app.filing_categories.service.IListService;
import com.Aeb.AebDMS.app.filing_categories.service.IMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListServiceImpl implements IListService {

    private final MetaDataListRepository metaDataListRepository;

    @Override
    public ListMetaData saveList(ListMetaData metadata) {
        return  metaDataListRepository.save(metadata);
    }

    @Override
    public Page<ListMetaData> findAll(String name,Pageable pageable) {
        if(name!=null && !name.isEmpty())
            return  metaDataListRepository.findAllByNameLike(name,pageable);
        return metaDataListRepository.findAll(pageable);
    }

    @Override
    public ListMetaData findById(Long id) {
        return metaDataListRepository.findById(id).orElseThrow(()->
                new BaseException("list not found", HttpStatus.NOT_FOUND)
                );
    }

    @Override
    public ListMetaData updateList(ListMetaData list) {
        return metaDataListRepository.save(list);
    }

    @Override
    public void deleteById(Long id) {
        metaDataListRepository.deleteById(id);
    }
}
