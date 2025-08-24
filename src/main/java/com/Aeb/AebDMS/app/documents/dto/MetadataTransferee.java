package com.Aeb.AebDMS.app.documents.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetadataTransferee {
    private String metaDataName;

    private String value;

    private String categoryName;

    private Long categoryId;
    private Long metadataId;
    private Long documentId;
}
