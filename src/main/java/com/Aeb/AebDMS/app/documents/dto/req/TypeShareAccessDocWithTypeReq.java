package com.Aeb.AebDMS.app.documents.dto.req;

import com.Aeb.AebDMS.shared.util.GranteeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAccessDocWithTypeReq {
    @NotBlank
    @Size(min = 1)
    String granteeId;

    @Valid
    DocumentPermissionReq permission;


    GranteeType type;
}
