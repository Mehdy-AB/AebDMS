package com.Aeb.AebDMS.app.elastic.dto.res;

import com.Aeb.AebDMS.app.documents.dto.res.DocumentResponseDto;
import com.Aeb.AebDMS.app.folders.dto.res.FolderResDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


@Data
@Builder
public class GlobalSearchResultDto {

    @Builder
    @Data
    public static class SearchFoldersRes {
        private final FolderResDto folder;
        private final float score;
        private final Map<String,String> highlight;
    }

    @Builder
    @Data
    public static class SearchDocumentsRes {
        private final DocumentResponseDto document;
        private final float score;
        private final Map<String,String> highlight;
    }

    private final List<SearchFoldersRes> folders;
    private final List<SearchDocumentsRes> documents;


    private final Pageable page;
}

