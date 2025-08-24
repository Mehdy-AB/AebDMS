package com.Aeb.AebDMS.app.documents.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAccesDoc {
    @NotBlank
    @Size(min = 1)
    String id;

    @Valid
    DocumentPermissionReq permission;
}
