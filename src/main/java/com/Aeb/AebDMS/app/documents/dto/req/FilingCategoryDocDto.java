package com.Aeb.AebDMS.app.documents.dto.req;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FilingCategoryDocDto {
    Long id;
    List<MetaDataDto> metaDataDto;
}
