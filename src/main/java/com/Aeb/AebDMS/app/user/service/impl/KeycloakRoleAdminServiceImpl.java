package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.app.CustomRoleRepresentation;
import com.Aeb.AebDMS.app.user.dto.role.RoleCreateReq;
import com.Aeb.AebDMS.app.user.dto.role.RoleUpdateReq;
import com.Aeb.AebDMS.app.user.service.IKeycloakRoleAdminService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakRoleAdminServiceImpl implements IKeycloakRoleAdminService {

    private final RealmResource realm;
    private static final String CLIENT_ID = "dms-backend";

    private String getClientId() {
        return realm.clients().findByClientId(CLIENT_ID).get(0).getId();
    }

    @Override
    public RoleDto createRole(RoleCreateReq req) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(req.getName());
        role.setDescription(req.getDescription());

        realm.roles().create(role);
        RoleRepresentation created = realm.roles().get(req.getName()).toRepresentation();

        if (req.getPermissions() != null) {
            for (String perm : req.getPermissions()) {
                assignPermissionToRole(created.getName(), perm);
            }
        }

        return toDto(created);
    }

    @Override
    public Optional<RoleDto> getRoleById(String roleId) {
        try {
            RoleRepresentation role = realm.roles().get(roleId).toRepresentation();
            return Optional.of(toDto(role));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return realm.roles().list().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RoleDto updateRole(String roleId, RoleUpdateReq req) {
        var roleResource = realm.roles().get(roleId);
        RoleRepresentation role = roleResource.toRepresentation();

        // update description
        if (req.getDescription() != null) {
            role.setDescription(req.getDescription());
        }
        roleResource.update(role);

        if (req.getPermissions() != null) {
            // remove old permissions (composites)
            Set<RoleRepresentation> currentComposites = roleResource.getRoleComposites();
            if (!currentComposites.isEmpty()) {
                roleResource.deleteComposites(currentComposites.stream().toList());
            }

            // assign new permissions
            for (String perm : req.getPermissions()) {
                assignPermissionToRole(role.getName(), perm);
            }
        }

        return toDto(roleResource.toRepresentation());
    }


    @Override
    public void deleteRole(String roleId) {
        realm.roles().get(roleId).remove();
    }

    @Override
    public void assignRoleToUser(String userId, String roleId) {
        UserResource user = realm.users().get(userId);
        RoleRepresentation role = realm.roles().get(roleId).toRepresentation();
        user.roles().realmLevel().add(List.of(role));
    }

    @Override
    public void removeRoleFromUser(String userId, String roleId) {
        UserResource user = realm.users().get(userId);
        RoleRepresentation role = realm.roles().get(roleId).toRepresentation();
        user.roles().realmLevel().remove(List.of(role));
    }

    @Override
    public void assignPermissionToRole(String roleId, String permission) {
        var client = realm.clients().get(getClientId());
        RoleRepresentation permRole = client.roles().get(permission).toRepresentation();
        realm.roles().get(roleId).addComposites(List.of(permRole));
    }

    @Override
    public void removePermissionFromRole(String roleId, String permission) {
        var client = realm.clients().get(getClientId());
        RoleRepresentation permRole = client.roles().get(permission).toRepresentation();
        realm.roles().get(roleId).deleteComposites(List.of(permRole));
    }

    private RoleDto toDto(RoleRepresentation r) {
        return new CustomRoleRepresentation(r,realm.roles().get(r.getName()).getRoleComposites()).getRole();
    }
}
