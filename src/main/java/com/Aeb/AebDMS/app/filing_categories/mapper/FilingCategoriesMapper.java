package com.Aeb.AebDMS.app.filing_categories.mapper;

import com.Aeb.AebDMS.app.filing_categories.dto.req.FilingCategoryRequestDto;
import com.Aeb.AebDMS.app.filing_categories.dto.res.FilingCategoryResponseDto;
import com.Aeb.AebDMS.app.filing_categories.model.CategoryMetadataDefinition;
import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilingCategoriesMapper {
    private final CategoryMetadataDefinitionMapper categoryMetadataDefinitionMapper;

    public FilingCategory toFilingCategory(FilingCategoryRequestDto filingCategoryRequestDto,String createdBy){
        FilingCategory category = FilingCategory.builder()
                .name(filingCategoryRequestDto.getName())
                .description(filingCategoryRequestDto.getDescription())
                .createdBy(createdBy)
                .build();

        category.setMetadataDefinitions(categoryMetadataDefinitionMapper.toEntityList(filingCategoryRequestDto.getMetadataDefinitions(),category));
        return category;
    }

    public FilingCategoryResponseDto toFilingCategoryDto(FilingCategory filingCategory){

        return FilingCategoryResponseDto.builder()
                .id(filingCategory.getId())
                .name(filingCategory.getName())
                .description(filingCategory.getDescription())
                .createdBy(filingCategory.getCreatedBy())
                .metadataDefinitions(categoryMetadataDefinitionMapper.toDtoList(filingCategory.getMetadataDefinitions()))
                .build();
    }

//    @AfterMapping
//    default void setCategoryReference(@MappingTarget FilingCategory category) {
//        if (category.getMetadataDefinitions() != null) {
//            for (CategoryMetadataDefinition def : category.getMetadataDefinitions()) {
//                def.setCategory(category);
//            }
//        }
//    }
}
