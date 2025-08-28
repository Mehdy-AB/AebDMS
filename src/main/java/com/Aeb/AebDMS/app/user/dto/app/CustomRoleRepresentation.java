package com.Aeb.AebDMS.app.user.dto.app;

import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomRoleRepresentation {
    private final RoleRepresentation delegate;
    private final Set<RoleRepresentation> roleComposites;

    public CustomRoleRepresentation(RoleRepresentation delegate, Set<RoleRepresentation> roleComposites) {
        this.delegate = delegate;
        this.roleComposites = roleComposites;
    }

    public RoleDto getRole() {

        return RoleDto.builder()
                .id(delegate.getId())
                .name(delegate.getName())
                .description(delegate.getDescription())
                .permissions(roleComposites.stream().map(RoleRepresentation::getName).collect(Collectors.toSet()))
                .build();
    }
}

