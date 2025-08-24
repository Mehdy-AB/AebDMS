package com.Aeb.AebDMS.app.folders.dto.res;

import com.Aeb.AebDMS.app.folders.dto.req.FolderPermissionReq;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeShareAccessRes {
    Object grantee;

    FolderPermissionReq permission;
}
