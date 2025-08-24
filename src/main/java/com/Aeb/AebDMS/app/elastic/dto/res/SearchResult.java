package com.Aeb.AebDMS.app.elastic.dto.res;

import com.Aeb.AebDMS.app.elastic.dto.SearchResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;


@Data
@AllArgsConstructor
@Builder
public class SearchResult {

    private final SearchResultType type;
    private final float score;
    private final Long objectId;
    private final Map<String,String> highlight;

}