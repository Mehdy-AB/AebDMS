package com.Aeb.AebDMS.app.elastic.repository;

import com.Aeb.AebDMS.app.elastic.model.MetadataDocumentElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataDocumentRepository extends ElasticsearchRepository<MetadataDocumentElastic, String> {
    // Add custom Elasticsearch queries here
}
