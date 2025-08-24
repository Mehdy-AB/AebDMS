package com.Aeb.AebDMS.app.elastic.controller;

import com.Aeb.AebDMS.app.elastic.dto.req.Filters;
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
            @RequestBody(required = true) Filters searchRequest,
            @AuthenticationPrincipal Jwt jwt
    ) {
        GlobalSearchResultDto response = searchServiceElastic.unifiedSearch(query,searchRequest,page,size, jwt.getSubject(),0);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
