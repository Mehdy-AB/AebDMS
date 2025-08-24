package com.Aeb.AebDMS.app.user.dto.app;

import com.Aeb.AebDMS.app.user.dto.GroupDto;
import org.keycloak.representations.idm.GroupRepresentation;

public class CustomGroupRepresentation {
    private final GroupRepresentation delegate;

    public CustomGroupRepresentation(GroupRepresentation delegate) {
        this.delegate = delegate;
    }

    public GroupDto getGroup() {
        return GroupDto.builder()
                .id(delegate.getId())
                .name(delegate.getName())
                .description(delegate.getDescription())
                .build();
    }
}

