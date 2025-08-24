package com.Aeb.AebDMS.app.user.dto.app;

import com.Aeb.AebDMS.app.user.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.util.List;

public class CustomUserRepresentation {
    private final UserRepresentation delegate;

    public CustomUserRepresentation(UserRepresentation delegate) {
        this.delegate = delegate;
    }

    private String[] safeGetJob() {
        if (delegate.getAttributes() == null) return new String[0];
        List<String> jobs = delegate.getAttributes().get("job");
        return jobs != null ? ((List<?>) jobs).toArray(new String[0]) : new String[0];
    }

    private String safeGetImage() {
        if (delegate.getAttributes() == null) return null;
        List<String> images = delegate.getAttributes().get("image");
        return (images != null && !images.isEmpty()) ? images.getFirst() : null;
    }

    public UserDto getUser() {
        return UserDto.builder()
                .id(delegate.getId())
                .username(delegate.getUsername())
                .email(delegate.getEmail())
                .firstName(delegate.getFirstName())
                .lastName(delegate.getLastName())
                .createdTimestamp(Instant.ofEpochMilli(delegate.getCreatedTimestamp()))
                .jobTitle(safeGetJob())
                .imageUrl(safeGetImage())
                .build();
    }
}
