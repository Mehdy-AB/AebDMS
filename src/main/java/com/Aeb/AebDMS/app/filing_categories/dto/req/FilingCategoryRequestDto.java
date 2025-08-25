package com.Aeb.AebDMS.app.filing_categories.dto.req;


import com.Aeb.AebDMS.app.filing_categories.dto.CategoryMetadataDefinitionDto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
public class FilingCategoryRequestDto {

    @NotBlank
    private String name;

    private String description;

    /** Nested metadata definitions to create */
    private List<CategoryMetadataDefinitionDto> metadataDefinitions;
}
