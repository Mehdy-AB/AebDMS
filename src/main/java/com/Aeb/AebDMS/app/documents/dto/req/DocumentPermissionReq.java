package com.Aeb.AebDMS.app.documents.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentPermissionReq {
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

}
