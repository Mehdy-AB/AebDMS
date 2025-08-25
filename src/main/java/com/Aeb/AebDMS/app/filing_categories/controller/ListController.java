package com.Aeb.AebDMS.app.filing_categories.controller;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.filing_categories.dto.req.FilingCategoryRequestDto;
import com.Aeb.AebDMS.app.filing_categories.dto.req.MetaDataListReq;
import com.Aeb.AebDMS.app.filing_categories.dto.res.FilingCategoryResponseDto;
import com.Aeb.AebDMS.app.filing_categories.dto.res.MetaDataListRes;
import com.Aeb.AebDMS.app.filing_categories.mapper.FilingCategoriesMapper;
import com.Aeb.AebDMS.app.filing_categories.mapper.ListMapper;
import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import com.Aeb.AebDMS.app.filing_categories.service.IFilingCategoryService;
import com.Aeb.AebDMS.app.filing_categories.service.IListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.v1.path}/filing-categories/list")
public class ListController {

    private final IListService service;
    private final ListMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_WRITE)")
    public ResponseEntity<MetaDataListRes> create(@RequestBody MetaDataListReq dto) {
        ListMetaData list = mapper.toEntity(dto);
        try {
            ListMetaData saved = service.saveList(list);

            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(saved));
        }catch (Exception ex) {
            System.out.println(ex);
            throw new BaseException( list.getName()+" already exists.",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public Page<MetaDataListRes> getAll(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam(value = "size",defaultValue = "20") Integer size
    ) {
        return service.findAll(name,PageRequest.of(page,size)).map(mapper::toDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaDataListRes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_UPDATE)")
    public ResponseEntity<MetaDataListRes> update(@NonNull @PathVariable Long id, @RequestBody MetaDataListReq dto) {
        dto.setId(id);
        ListMetaData updated = mapper.toEntity(dto);

        ListMetaData saved = service.updateList(updated);
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_DELETE)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
