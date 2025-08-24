package com.Aeb.AebDMS.app.documents.dto.req;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaDataDto {
    Long id;
    String value;
}
