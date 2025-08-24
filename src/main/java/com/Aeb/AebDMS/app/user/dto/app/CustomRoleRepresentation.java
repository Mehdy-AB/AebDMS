package com.Aeb.AebDMS.app.user.dto.app;

import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

public class CustomRoleRepresentation {
    private final RoleRepresentation delegate;

    public CustomRoleRepresentation(RoleRepresentation delegate) {
        this.delegate = delegate;
    }

    public RoleDto getRole() {
        return RoleDto.builder()
                .id(delegate.getId())
                .name(delegate.getName())
                .description(delegate.getDescription())
                .build();
    }
}

