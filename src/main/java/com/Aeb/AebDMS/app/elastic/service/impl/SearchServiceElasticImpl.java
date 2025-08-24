package com.Aeb.AebDMS.app.elastic.service.impl;


import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.Aeb.AebDMS.app.documents.mapper.DocumentMapper;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.documents.repository.DocumentVersionRepository;
import com.Aeb.AebDMS.app.documents.service.IDocumentService;
import com.Aeb.AebDMS.app.elastic.dto.SearchResultType;
import com.Aeb.AebDMS.app.elastic.dto.req.Filters;
import com.Aeb.AebDMS.app.elastic.dto.req.SortFields;
import com.Aeb.AebDMS.app.elastic.dto.res.GlobalSearchResultDto;
import com.Aeb.AebDMS.app.elastic.model.DocumentVersionDocumentElastic;
import com.Aeb.AebDMS.app.elastic.model.FolderElastic;
import com.Aeb.AebDMS.app.elastic.service.IDocumentVersionDocumentServiceElastic;
import com.Aeb.AebDMS.app.elastic.service.IFolderElasticService;
import com.Aeb.AebDMS.app.elastic.service.ISearchServiceElastic;
import com.Aeb.AebDMS.app.folders.mapper.FolderMapper;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.repository.FolderRepository;
import com.Aeb.AebDMS.app.folders.service.IFolderService;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.Aeb.AebDMS.app.elastic.dto.res.SearchResult;


@Service
@RequiredArgsConstructor
public class SearchServiceElasticImpl implements ISearchServiceElastic {
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final IKeycloakUserService keycloakService;
    private final IDocumentVersionDocumentServiceElastic documentVersionDocumentServiceElastic;
    private final IFolderElasticService folderElasticService;
    private final DocumentMapper documentMapper;
    private final IFolderService folderService;
    private final IDocumentService documentService;
    private final DocumentVersionRepository documentVersionRepository;
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    private static final int MAX_RETRIES = 1;

    public GlobalSearchResultDto unifiedSearch(String query, Filters searchRequest, int page, int size, String userId, int attempt) {
        // user ids for ES permission filter
        List<String> ids = keycloakService.getAllUserGroupAndRolesId(userId);
        ids.add(userId);

        NativeQuery searchQuery = buildSearchQuery(query, searchRequest, page, size, ids);
        IndexCoordinates indices = buildIndexCoordinates(searchRequest);

        // 1) execute ES search
        SearchHits<Object> searchHits = elasticsearchTemplate.search(searchQuery, Object.class, indices);
        List<SearchResult> esResults = searchHits.getSearchHits().stream()
                .map(this::mapToSearchResult)
                .toList();

        // 2) collect ids in ES order
        List<Long> folderIds = esResults.stream()
                .filter(r -> r.getType() == SearchResultType.FOLDER)
                .map(SearchResult::getObjectId)
                .distinct()
                .toList();

        List<Long> docIds = esResults.stream()
                .filter(r -> r.getType() == SearchResultType.DOCUMENT)
                .map(SearchResult::getObjectId)
                .distinct()
                .toList();

        // 3) batch fetch DB objects (no permission logic here)
        List<Folder> foldersFound = folderRepository.findAllById(folderIds);
        Map<Long, Folder> folderMap = foldersFound.stream().collect(Collectors.toMap(Folder::getId, f -> f));

        // fetch latest versions in batch for docIds (you must implement this JPQL method)
        List<DocumentVersion> latestVersions = documentVersionRepository.findLatestVersionsByDocumentIds(docIds);
        Map<Long, DocumentVersion> latestByDocId = latestVersions.stream()
                .collect(Collectors.toMap(DocumentVersion::getDocumentId, v -> v));

        // 4) decide which results are valid or need cleanup
        Set<Long> toDeleteEsFolderIds = new HashSet<>();
        Set<Long> toDeleteEsDocIds = new HashSet<>();
        Set<Long> toRemoveGrantFromFolders = new HashSet<>();
        Set<Long> toRemoveGrantFromDocs = new HashSet<>();

        // prepare user ids set for permission checks once
        Set<String> userIdSet = new HashSet<>(ids);

        // iterate ES results preserving ES order and build final lists
        List<GlobalSearchResultDto.SearchFoldersRes> folders = new ArrayList<>();
        List<GlobalSearchResultDto.SearchDocumentsRes> documents = new ArrayList<>();

        for (SearchResult sr : esResults) {
            if (sr.getType() == SearchResultType.FOLDER) {
                Folder folder = folderMap.get(sr.getObjectId());
                if (folder == null) {
                    // DB row missing -> remove ES doc
                    toDeleteEsFolderIds.add(sr.getObjectId());
                    continue;
                }
                // check permission (use your existing haveAccessRead logic but without DB fetch)
                if (!folderService.haveAccessRead(folder, userId)) {
                    // permission mismatch -> remove the user's ids from ES grantedIds (batch)
                    toRemoveGrantFromFolders.add(sr.getObjectId());
                    continue;
                }
                folders.add(GlobalSearchResultDto.SearchFoldersRes.builder()
                                .folder(folderMapper.toResponseDto(folder))
                                .score(sr.getScore())
                                .highlight(sr.getHighlight())
                        .build());
            } else {
                DocumentVersion dv = latestByDocId.get(sr.getObjectId());
                if (dv == null) {
                    // doc not found -> delete ES doc
                    toDeleteEsDocIds.add(sr.getObjectId());
                    continue;
                }
                // check permission using canRead (but use dv and its document/folder already loaded)
                if (!documentService.canRead(dv.getDocument().getFolder(), userId, dv.getDocumentId())) {
                    toRemoveGrantFromDocs.add(sr.getObjectId());
                    continue;
                }
                documents.add(GlobalSearchResultDto.SearchDocumentsRes.builder()
                                .document(documentMapper.toDocumentUploadResponseDto(dv))
                                .score(sr.getScore())
                                .highlight(sr.getHighlight())
                        .build());
            }
        }

        // 5) if any cleanup required, do it in batch (no recursion)
        if (!toDeleteEsFolderIds.isEmpty() || !toDeleteEsDocIds.isEmpty() ||
                !toRemoveGrantFromFolders.isEmpty() || !toRemoveGrantFromDocs.isEmpty()) {
            System.out.println("iam here:{}");
            // delete ES docs whose DB rows were removed
            if (!toDeleteEsFolderIds.isEmpty()) {
                folderElasticService.deleteByIds(toDeleteEsFolderIds); // implement repository.deleteAllByIdInBatch
            }
            if (!toDeleteEsDocIds.isEmpty()) {
                documentVersionDocumentServiceElastic.deleteByIds(toDeleteEsDocIds);
            }

            // remove user's ids from grantedIds using ES update script (atomic) or batch partial update
            if (!toRemoveGrantFromFolders.isEmpty()) {
                folderElasticService.removeGrantedIdsFromDocs(userIdSet, toRemoveGrantFromFolders);
            }
            if (!toRemoveGrantFromDocs.isEmpty()) {
                documentVersionDocumentServiceElastic.removeGrantedIdsFromDocs(userIdSet, toRemoveGrantFromDocs);
            }

            if(attempt<MAX_RETRIES)
                return unifiedSearch(query, searchRequest, page, size, userId, ++attempt);
        }

        // 6) build final DTO (page info from searchQuery pageable)
        return GlobalSearchResultDto.builder()
                .documents(documents)
                .folders(folders)
                .page(searchQuery.getPageable())
                .build();
    }


    private SearchResult mapToSearchResult(SearchHit<?> hit) {
        String index = hit.getIndex();
        Object content = hit.getContent();
        Map<String, String> highlightMap = getStringStringMap(hit);

        if (index!=null&&index.equals("folders")) {
            FolderElastic folder = (FolderElastic) content;
            return SearchResult.builder()
                    .type(SearchResultType.FOLDER)
                    .objectId(folder.getId())
                    .score(hit.getScore())
                    .highlight(highlightMap)
                    .build();
        } else if (index!=null&&index.equals("document_versions")) {
            DocumentVersionDocumentElastic doc = (DocumentVersionDocumentElastic) content;
            return SearchResult.builder()
                    .type(SearchResultType.DOCUMENT)
                    .objectId(doc.getId())
                    .score(hit.getScore())
                    .highlight(highlightMap)
                    .build();
        }

        throw new IllegalStateException("Unknown search index: " + index);
    }

    private static Map<String, String> getStringStringMap(SearchHit<?> hit) {
        Map<String, List<String>> highlightFields = hit.getHighlightFields(); // highlights from ES

        // List of all fields we highlighted in ES
        List<String> allHighlightFields = List.of(
                "name",
                "ocrText",
                "metadata.key",
                "metadata.categoryName",
                "metadata.value",
                "description"
        );

        // Build map of field -> highlighted text (first fragment or null if not present)
        Map<String, String> highlightMap = new HashMap<>();
        for (String field : allHighlightFields) {
            List<String> fragments = highlightFields.get(field);
            highlightMap.put(field, (fragments != null && !fragments.isEmpty()) ? fragments.getFirst() : null);
        }
        return highlightMap;
    }


    private List<String> buildFields(Filters filters) {
        List<String> fields = new ArrayList<>();

        // Always include name if allowed
        if (Boolean.TRUE.equals(filters.getLookUpFolderName()) || Boolean.TRUE.equals(filters.getLookUpDocumentName())) {
            fields.add("name^4");
        }

        if (Boolean.TRUE.equals(filters.getLookUpOcrContent())) {
            fields.add("ocrText^3");
        }
        if (Boolean.TRUE.equals(filters.getLookUpMetadataKey())) {
            fields.add("metadata.key");
        }
        if (Boolean.TRUE.equals(filters.getLookUpCategoryName())) {
            fields.add("metadata.categoryName");
        }
        if (Boolean.TRUE.equals(filters.getLookUpMetadataValue())) {
            fields.add("metadata.value^2");
        }
        if (Boolean.TRUE.equals(filters.getLookUpDescription())) {
            fields.add("description^2");
        }

        return fields;
    }


    private IndexCoordinates buildIndexCoordinates(Filters filters) {
        List<String> indices = new ArrayList<>();
        if (Boolean.TRUE.equals(filters.getIncludeDocuments())) {
            indices.add("document_versions");
        }
        if (Boolean.TRUE.equals(filters.getIncludeFolders())) {
            indices.add("folders");
        }
        if(indices.isEmpty()) {
            indices.add("folders");
        }
        return IndexCoordinates.of(indices.toArray(new String[0]));
    }


    public NativeQuery buildSearchQuery(String query,
                                        Filters searchRequest,
                                        int page,
                                        int size,
                                        List<String> grantedIds) {

        List<String> fields = buildFields(searchRequest);

        List<FieldValue> fv = grantedIds.stream()
                .map(FieldValue::of)
                .toList();

        // Highlight fields: remove boost (^4, ^3) from field names
        List<String> highlightFields = fields.stream()
                .map(f -> f.replaceAll("\\^\\d+$", ""))
                .distinct()
                .toList();

        HighlightQuery highlightQuery = new HighlightQuery(new Highlight(highlightFields.stream().map(HighlightField::new).toList()),String.class);

        // Determine sort
        Sort sort;
        if (SortFields.name.equals(searchRequest.getSortBy())) {
            sort = Sort.by(searchRequest.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, "name.keyword");
        } else if (SortFields.createdAt.equals(searchRequest.getSortBy())) {
            sort = Sort.by(searchRequest.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        } else if (SortFields.updatedAt.equals(searchRequest.getSortBy())) {
            sort = Sort.by(searchRequest.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC, "updatedAt");
        } else {
            // default: score
            sort = Sort.by(Sort.Direction.DESC, "_score");
        }

        // Build NativeQuery
        return NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields(fields)
                                                .type(TextQueryType.BestFields)
                                        )
                                )
                                .filter(f -> f
                                        .terms(t -> t
                                                .field("grantedIds")
                                                .terms(ts -> ts.value(fv))
                                        )
                                )
                        )
                )
                // Configure highlighting
                .withHighlightQuery(highlightQuery)
                .withPageable(PageRequest.of(page, size, sort))
                .build();
    }
}
