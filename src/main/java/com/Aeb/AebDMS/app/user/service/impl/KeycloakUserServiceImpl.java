package com.Aeb.AebDMS.app.user.service.impl;


import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.app.CustomGroupRepresentation;
import com.Aeb.AebDMS.app.user.dto.app.CustomRoleRepresentation;
import com.Aeb.AebDMS.app.user.dto.app.CustomUserRepresentation;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements IKeycloakUserService {
    private final RealmResource realm;

    @Override
    public Optional<UserDto> getUserById(String id) {
        try {
            UserRepresentation raw = realm.users().get(id).toRepresentation();
            CustomUserRepresentation custom = new CustomUserRepresentation(raw);
            return Optional.of(custom.getUser());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

//    @Override
//    public List<UserRepresentation> getUsersByIds(List<String> userIds) {
//        return userIds.parallelStream()
//                .map(id -> {
//                    try {
//                        return realm.users().get(id).toRepresentation();
//                    } catch (Exception e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<Boolean> getUsersExistingByIds(List<String> userIds) {
        return userIds.parallelStream()
                .map(id -> {
                    try {
                        realm.users().get(id).toRepresentation();
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> getRoleById(String roleIds) {
        try {
            return Optional.of(new CustomRoleRepresentation(realm.rolesById().getRole(roleIds)).getRole());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

//    @Override
//    public List<RoleRepresentation> getRolesByIds(List<String> roleIds) {
//        return roleIds.parallelStream()
//                .map(id -> {
//                    try {
//                        return realm.rolesById().getRole(id);
//                    } catch (Exception e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<Boolean> getRolesExistingByIds(List<String> roleIds) {
        return roleIds.parallelStream()
                .map(id -> {
                    try {
                        realm.rolesById().getRole(id);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupDto> getGroupById(String groupId) {
        try {
            return Optional.of(new CustomGroupRepresentation(realm.groups().group(groupId).toRepresentation()).getGroup());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GroupRepresentation> getGroupesByIds(List<String> groupIds) {
        return groupIds.parallelStream()
                .map(id -> {
                    try {
                        return realm.groups().group(id).toRepresentation();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Boolean> getGroupersExistingByIds(List<String> groupIds) {
        return groupIds.parallelStream()
                .map(id -> {
                    try {
                        realm.groups().group(id).toRepresentation();
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllUserGroupAndRolesId(String userId) {

        // Get all group IDs
        List<GroupRepresentation> groups = realm.users().get(userId).groups();
        List<String> ids = new ArrayList<>(groups.stream().map(GroupRepresentation::getId).toList());

        // Get all realm role names
        List<RoleRepresentation> roles = realm.users()
                .get(userId)
                .roles()
                .realmLevel()
                .listEffective();

        ids.addAll(roles.stream().map(RoleRepresentation::getId).toList());

        return ids;
    }

    @Override
    public Map<String, UserDto> getUsersByIds(Collection<String> ids) {
        Map<String, UserDto> result = new HashMap<>();
        for (String id : ids) {
            Optional<UserDto> user = getUserById(id);
            if (user.isPresent()) {
                result.put(id, user.get());
            } else {
                result.put(id, null);
            }
        }
        return result;
    }

    @Override
    public Map<String, GroupDto> getGroupsByIds(Collection<String> ids) {
        Map<String, GroupDto> result = new HashMap<>();
        for (String id : ids) {
            Optional<GroupDto> user = getGroupById(id);
            if (user.isPresent()) {
                result.put(id, user.get());
            } else {
                result.put(id, null);
            }
        }
        return result;
    }

    @Override
    public Map<String, RoleDto> getRolesByIds(Collection<String> ids) {
        Map<String, RoleDto> result = new HashMap<>();
        for (String id : ids) {
            Optional<RoleDto> user = getRoleById(id);
            if (user.isPresent()) {
                result.put(id, user.get());
            } else {
                result.put(id, null);
            }
        }
        return result;
    }
}
