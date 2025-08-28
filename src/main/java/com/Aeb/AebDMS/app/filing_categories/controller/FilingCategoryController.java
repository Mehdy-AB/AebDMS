package com.Aeb.AebDMS.app.filing_categories.controller;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.filing_categories.dto.req.FilingCategoryRequestDto;
import com.Aeb.AebDMS.app.filing_categories.dto.res.FilingCategoryResponseDto;
import com.Aeb.AebDMS.app.filing_categories.mapper.FilingCategoriesMapper;
import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import com.Aeb.AebDMS.app.filing_categories.service.IFilingCategoryService;
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
@RequestMapping("${app.v1.path}/filing-categories")
public class FilingCategoryController {

    private final IFilingCategoryService service;
    private final FilingCategoriesMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_WRITE)")
    public ResponseEntity<FilingCategoryResponseDto> create(@RequestBody FilingCategoryRequestDto dto, @AuthenticationPrincipal Jwt jwt) {
        FilingCategory category = mapper.toFilingCategory(dto, jwt.getSubject());
        category.setCreatedBy(jwt.getSubject());
        try {
            FilingCategory saved = service.saveFilingCategory(category);

            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toFilingCategoryDto(saved));
        }catch (Exception ex) {
            System.out.println(ex);
            throw new BaseException( category.getName()+" already exists.",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public Page<FilingCategoryResponseDto> getAll(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "page",defaultValue = "0") Integer page, @RequestParam(value = "size",defaultValue = "20") Integer size
    ) {
        return service.findAll(name,PageRequest.of(page,size)).map(mapper::toFilingCategoryDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilingCategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toFilingCategoryDto(service.findById(id)));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_UPDATE)")
    public ResponseEntity<FilingCategoryResponseDto> update(@NonNull @PathVariable Long id, @RequestBody FilingCategoryRequestDto dto, @AuthenticationPrincipal Jwt jwt) {

        FilingCategory updated = mapper.toFilingCategory(dto, jwt.getSubject());
        updated.setId(id); // ensure ID is passed

        FilingCategory saved = service.updateFilingCategory(updated);
        return ResponseEntity.ok(mapper.toFilingCategoryDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.Aeb.AebDMS.shared.util.Permissions).MODEL_DELETE)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
