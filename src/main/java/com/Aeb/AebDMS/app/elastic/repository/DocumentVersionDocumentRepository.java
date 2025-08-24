package com.Aeb.AebDMS.app.elastic.repository;

import com.Aeb.AebDMS.app.elastic.model.DocumentVersionDocumentElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVersionDocumentRepository extends ElasticsearchRepository<DocumentVersionDocumentElastic, String> {
    // Add custom Elasticsearch queries here
}
