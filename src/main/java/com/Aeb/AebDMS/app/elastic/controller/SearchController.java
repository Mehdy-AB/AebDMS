package com.Aeb.AebDMS.app.elastic.controller;

import com.Aeb.AebDMS.app.elastic.dto.req.Filters;
import com.Aeb.AebDMS.app.elastic.dto.req.SortFields;
import com.Aeb.AebDMS.app.elastic.dto.res.GlobalSearchResultDto;
import com.Aeb.AebDMS.app.elastic.service.ISearchServiceElastic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.v1.path}/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearchServiceElastic searchServiceElastic;


    @GetMapping
    public ResponseEntity<GlobalSearchResultDto> upload(
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam(value = "size",defaultValue = "20") Integer size,
            @RequestParam(value = "query",defaultValue = "20") String query,
            @RequestParam(value = "lookUpFolderName",defaultValue = "true") Boolean lookUpFolderName,
            @RequestParam(value = "lookUpDocumentName",defaultValue = "true") Boolean lookUpDocumentName,
            @RequestParam(value = "lookUpMetadataKey",defaultValue = "false") Boolean lookUpMetadataKey,
            @RequestParam(value = "lookUpMetadataValue",defaultValue = "true") Boolean lookUpMetadataValue,
            @RequestParam(value = "lookUpCategoryName",defaultValue = "false") Boolean lookUpCategoryName,
            @RequestParam(value = "lookUpOcrContent",defaultValue = "true") Boolean lookUpOcrContent,
            @RequestParam(value = "lookUpDescription",defaultValue = "true") Boolean lookUpDescription,
            @RequestParam(value = "quincludeFoldersery",defaultValue = "true") Boolean includeFolders,
            @RequestParam(value = "includeDocuments",defaultValue = "true") Boolean includeDocuments,
            @RequestParam(value = "sortBy",defaultValue = "score") SortFields sortBy,
            @RequestParam(value = "sortDesc",defaultValue = "true") Boolean sortDesc,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Filters searchRequest = Filters.builder()
                .lookUpFolderName(lookUpFolderName)
                .lookUpDocumentName(lookUpDocumentName)
                .includeDocuments(includeDocuments)
                .includeFolders(includeFolders)
                .lookUpCategoryName(lookUpCategoryName)
                .lookUpMetadataKey(lookUpMetadataKey)
                .lookUpMetadataValue(lookUpMetadataValue)
                .lookUpDescription(lookUpDescription)
                .sortBy(sortBy)
                .sortDesc(sortDesc)
                .build();

        GlobalSearchResultDto response = searchServiceElastic.unifiedSearch(query,searchRequest,page,size, jwt.getSubject(),0);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
