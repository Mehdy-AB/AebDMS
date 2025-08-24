package com.Aeb.AebDMS.app.elastic.service;

import java.util.List;

import com.Aeb.AebDMS.app.elastic.dto.req.Filters;
import com.Aeb.AebDMS.app.elastic.dto.res.GlobalSearchResultDto;
import com.Aeb.AebDMS.app.elastic.dto.res.SearchResult;
import org.springframework.data.domain.Page;


public interface ISearchServiceElastic {
    public GlobalSearchResultDto unifiedSearch(String query, Filters searchRequest, int page, int size, String userId, int attempt);
}
