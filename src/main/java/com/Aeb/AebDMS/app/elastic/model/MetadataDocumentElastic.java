package com.Aeb.AebDMS.app.elastic.model;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.*;

//@Document(indexName = "document_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDocumentElastic {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String key;

    @Field(type = FieldType.Text)
    private String value;

    @Field(type = FieldType.Text)
    private String categoryName;

    private Long categoryId;
    private Long metadataId;

}
