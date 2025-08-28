package com.Aeb.AebDMS.app.user.service.impl;


import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.app.CustomGroupRepresentation;
import com.Aeb.AebDMS.app.user.dto.app.CustomUserRepresentation;
import com.Aeb.AebDMS.app.user.dto.group.GroupCreateReq;
import com.Aeb.AebDMS.app.user.dto.group.GroupUpdateReq;
import com.Aeb.AebDMS.app.user.service.IKeycloakGroupAdminService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakGroupAdminServiceImpl implements IKeycloakGroupAdminService {

    private final RealmResource realm;

    @Override
    public GroupDto createGroup(GroupCreateReq req) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(req.getName());
        if (req.getPath() != null) group.setPath(req.getPath());

        var response = realm.groups().add(group);
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to create group: " + response.getStatus());
        }

        String groupId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        return getGroupById(groupId).orElseThrow();
    }

    @Override
    public Optional<GroupDto> getGroupById(String groupId) {
        try {
            GroupRepresentation group = realm.groups().group(groupId).toRepresentation();
            return Optional.of(toDto(group));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GroupDto> getAllGroups(int first, int max) {
        return realm.groups().groups(first, max)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public GroupDto updateGroup(String groupId, GroupUpdateReq req) {
        GroupResource groupResource = realm.groups().group(groupId);
        GroupRepresentation group = groupResource.toRepresentation();

        if (req.getName() != null) group.setName(req.getName());

        groupResource.update(group);
        return toDto(groupResource.toRepresentation());
    }

    @Override
    public void deleteGroup(String groupId) {
        realm.groups().group(groupId).remove();
    }

    @Override
    public void addUserToGroup(String userId, String groupId) {
        UserResource user = realm.users().get(userId);
        user.joinGroup(groupId);
    }

    @Override
    public void removeUserFromGroup(String userId, String groupId) {
        UserResource user = realm.users().get(userId);
        user.leaveGroup(groupId);
    }

    @Override
    public List<UserDto> getGroupMembers(String groupId, int first, int max) {
        GroupResource group = realm.groups().group(groupId);
        List<UserRepresentation> members = group.members(first, max);
        return members.stream().map(this::toUserDto).collect(Collectors.toList());
    }

    private GroupDto toDto(GroupRepresentation g) {
        return new CustomGroupRepresentation(g).getGroup();
    }

    private UserDto toUserDto(UserRepresentation u) {
        return new CustomUserRepresentation(u).getUser();
    }
}