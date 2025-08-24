package com.Aeb.AebDMS.app.documents.dto.res;

import com.Aeb.AebDMS.app.documents.dto.req.DocumentPermissionReq;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAccessDocumentRes {
    Object grantee;

    DocumentPermissionReq permission;
}
