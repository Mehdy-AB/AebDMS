package com.Aeb.AebDMS.app.filing_categories.dto.res;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MetaDataListRes {
    private Long id;

    private String name;

    private String description;

    private boolean mandatory;

    private List<String> option;
}
