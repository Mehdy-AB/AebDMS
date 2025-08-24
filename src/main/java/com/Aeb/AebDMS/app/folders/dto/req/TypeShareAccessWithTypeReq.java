package com.Aeb.AebDMS.app.folders.dto.req;

import com.Aeb.AebDMS.shared.util.GranteeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAccessWithTypeReq {
    @NotBlank
    @Size(min = 1)
    String granteeId;

    @Valid
    FolderPermissionReq permission;


    GranteeType type;
}
