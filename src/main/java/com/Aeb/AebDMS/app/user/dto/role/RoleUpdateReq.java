package com.Aeb.AebDMS.app.user.dto.role;

import lombok.Data;

import java.util.Set;

@Data
public class RoleUpdateReq {
    private String description;
    private Set<String> permissions;
}