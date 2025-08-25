package com.Aeb.AebDMS.app.filing_categories.service;

import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IListService {
    
    ListMetaData saveList(ListMetaData metadata);

    Page<ListMetaData> findAll(String name,Pageable pageable);
    
    ListMetaData findById(Long id);

    ListMetaData updateList(ListMetaData list);
    
    void deleteById(Long id);
}
