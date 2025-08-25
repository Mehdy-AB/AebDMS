package com.Aeb.AebDMS.app.elastic.repository;

import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderDocumentRepository extends ElasticsearchRepository<FolderElastic, Long> {
    // Add custom Elasticsearch queries here
}
