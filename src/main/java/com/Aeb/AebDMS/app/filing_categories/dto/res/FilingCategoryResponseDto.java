package com.Aeb.AebDMS.app.filing_categories.dto.res;

import com.Aeb.AebDMS.app.filing_categories.dto.CategoryMetadataDefinitionDto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class FilingCategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    /** Nested metadata definitions in the category */
    private List<CategoryMetadataDefinitionDto> metadataDefinitions;
}
