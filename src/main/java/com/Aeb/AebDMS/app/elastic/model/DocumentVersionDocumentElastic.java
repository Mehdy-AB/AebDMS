package com.Aeb.AebDMS.app.elastic.model;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.*;

@Document(indexName = "document_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionDocumentElastic {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Keyword)
    private Long folderId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String path;

    @Field(type = FieldType.Keyword)
    private String mimeType;

    @Field(type = FieldType.Keyword)
    private Long sizeBytes;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String ocrText;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;

    @Field(type = FieldType.Nested)
    private List<MetadataDocumentElastic> metadata;

    @Field(type=FieldType.Keyword)
    private List<String> grantedIds;

}