package com.Aeb.AebDMS.app.filing_categories.dto;

import com.Aeb.AebDMS.app.filing_categories.dto.req.MetaDataListReq;
import com.Aeb.AebDMS.app.filing_categories.model.MetadataType;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CategoryMetadataDefinitionDto {
    private String id; // will be null on create

    @NotBlank
    private String key;

    @NotNull
    private MetadataType dataType;

    @NotNull
    private Boolean mandatory;

    private Long listId;

    private MetaDataListReq list;
}