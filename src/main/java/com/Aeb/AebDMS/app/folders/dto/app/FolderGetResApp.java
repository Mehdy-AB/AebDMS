package com.Aeb.AebDMS.app.folders.dto.app;

import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.shared.util.PageMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class FolderGetResApp {
    private final Folder folder;
    private final List<FolderWithCountDto> folders;
    private final List<DocumentVersion> documents;
    private final PageMeta pageable;
}
