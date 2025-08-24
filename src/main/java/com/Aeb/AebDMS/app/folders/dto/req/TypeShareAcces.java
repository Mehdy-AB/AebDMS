package com.Aeb.AebDMS.app.folders.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAcces {
    @NotBlank
    @Size(min = 1)
    String id;

    @Valid
    FolderPermissionReq permission;
}
