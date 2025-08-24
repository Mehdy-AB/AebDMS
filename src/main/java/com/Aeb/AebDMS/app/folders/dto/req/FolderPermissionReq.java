package com.Aeb.AebDMS.app.folders.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderPermissionReq {
    @NotNull
    private boolean canView;

    @NotNull
    private boolean canUpload;

    @NotNull
    private boolean canEdit;

    @NotNull
    private boolean canDelete;

    @NotNull
    private boolean canShare;

    @NotNull
    private boolean canManagePermissions;

    @NotNull
    private boolean canCreateSubFolders;

    @NotNull
    private boolean canEditDoc;

    @NotNull
    private boolean canDeleteDoc;

    @NotNull
    private boolean canShareDoc;

    @NotNull
    private boolean canManagePermissionsDoc;

    @NotNull
    private boolean inherits;
}
