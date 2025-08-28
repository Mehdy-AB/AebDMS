package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.app.CustomGroupRepresentation;
import com.Aeb.AebDMS.app.user.dto.app.CustomRoleRepresentation;
import com.Aeb.AebDMS.app.user.dto.app.CustomUserRepresentation;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements IKeycloakUserService {

    private final RealmResource realm;

    // ✅ Caffeine caches (10k entries, expire after 10 mins)
    private final Cache<String, UserDto> userCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    private final Cache<String, GroupDto> groupCache = Caffeine.newBuilder()
            .maximumSize(5_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    private final Cache<String, RoleDto> roleCache = Caffeine.newBuilder()
            .maximumSize(5_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    @Override
    public Optional<UserDto> getUserById(String id) {
        return Optional.ofNullable(userCache.get(id, this::loadUser));
    }

    private UserDto loadUser(String id) {
        try {
            UserRepresentation raw = realm.users().get(id).toRepresentation();
            return new CustomUserRepresentation(raw).getUser();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boolean> getUsersExistingByIds(List<String> userIds) {
        return userIds.parallelStream()
                .map(id -> getUserById(id).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> getRoleById(String roleId) {
        return Optional.ofNullable(roleCache.get(roleId, this::loadRole));
    }

    private RoleDto loadRole(String id) {
        try {
            RoleRepresentation role =realm.rolesById().getRole(id);
            return new CustomRoleRepresentation(role,realm.roles().get(role.getName()).getRoleComposites()).getRole();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boolean> getRolesExistingByIds(List<String> roleIds) {
        return roleIds.parallelStream()
                .map(id -> getRoleById(id).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupDto> getGroupById(String groupId) {
        return Optional.ofNullable(groupCache.get(groupId, this::loadGroup));
    }

    private GroupDto loadGroup(String id) {
        try {
            return new CustomGroupRepresentation(realm.groups().group(id).toRepresentation()).getGroup();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boolean> getGroupersExistingByIds(List<String> groupIds) {
        return groupIds.parallelStream()
                .map(id -> getGroupById(id).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllUserGroupAndRolesId(String userId) {
        List<String> ids = new ArrayList<>();

        // groups
        List<GroupRepresentation> groups = realm.users().get(userId).groups();
        ids.addAll(groups.stream().map(GroupRepresentation::getId).toList());

        // roles
        List<RoleRepresentation> roles = realm.users().get(userId)
                .roles()
                .realmLevel()
                .listEffective();
        ids.addAll(roles.stream().map(RoleRepresentation::getId).toList());

        return ids;
    }

    // -------- Batch getters with caching --------

    @Override
    public Map<String, UserDto> getUsersByIds(Collection<String> ids) {
        return ids.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> getUserById(id).orElse(null)
                ));
    }

    @Override
    public Map<String, GroupDto> getGroupsByIds(Collection<String> ids) {
        return ids.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> getGroupById(id).orElse(null)
                ));
    }

    @Override
    public Map<String, RoleDto> getRolesByIds(Collection<String> ids) {
        return ids.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> getRoleById(id).orElse(null)
                ));
    }
}
