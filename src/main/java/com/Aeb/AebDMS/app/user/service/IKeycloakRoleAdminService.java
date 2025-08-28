package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.role.RoleCreateReq;
import com.Aeb.AebDMS.app.user.dto.role.RoleUpdateReq;

import java.util.List;
import java.util.Optional;

public interface IKeycloakRoleAdminService {
    RoleDto createRole(RoleCreateReq req);
    Optional<RoleDto> getRoleById(String roleId);
    List<RoleDto> getAllRoles();
    RoleDto updateRole(String roleId, RoleUpdateReq req);
    void deleteRole(String roleId);

    void assignRoleToUser(String userId, String roleId);
    void removeRoleFromUser(String userId, String roleId);

    void assignPermissionToRole(String roleId, String permission);
    void removePermissionFromRole(String roleId, String permission);
}

