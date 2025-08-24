package com.Aeb.AebDMS.app.filing_categories.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MetaDataListReq {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private boolean mandatory;

    @NotNull
    @Size(min = 1)
    private List<String> option;
}
