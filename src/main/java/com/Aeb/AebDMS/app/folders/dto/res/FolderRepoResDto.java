package com.Aeb.AebDMS.app.folders.dto.res;

import com.Aeb.AebDMS.app.documents.dto.res.DocumentResponseDto;
import com.Aeb.AebDMS.shared.util.PageMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class FolderRepoResDto {
    private final FolderResDto folder;
    private final List<FolderResDto> folders;
    private final List<DocumentResponseDto> documents;
    private final PageMeta pageable;
}
