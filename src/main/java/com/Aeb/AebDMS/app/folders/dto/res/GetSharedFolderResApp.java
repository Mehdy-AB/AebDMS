package com.Aeb.AebDMS.app.folders.dto.res;

import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.shared.util.PageMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class GetSharedFolderResApp {
    private List<FolderWithCountDto> folders;
    private List<DocumentVersion> documents;
    private PageMeta pageable;
}
