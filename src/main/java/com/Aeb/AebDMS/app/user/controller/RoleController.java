package com.Aeb.AebDMS.app.user.controller;

import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.role.RoleCreateReq;
import com.Aeb.AebDMS.app.user.dto.role.RoleUpdateReq;
import com.Aeb.AebDMS.app.user.service.IKeycloakRoleAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IKeycloakRoleAdminService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleCreateReq req) {
        return ResponseEntity.ok(roleService.createRole(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable String id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable String id, @RequestBody RoleUpdateReq req) {
        return ResponseEntity.ok(roleService.updateRole(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable String roleId, @PathVariable String userId) {
        roleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable String roleId, @PathVariable String userId) {
        roleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/permissions/{permission}")
    public ResponseEntity<Void> assignPermission(@PathVariable String roleId, @PathVariable String permission) {
        roleService.assignPermissionToRole(roleId, permission);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permission}")
    public ResponseEntity<Void> removePermission(@PathVariable String roleId, @PathVariable String permission) {
        roleService.removePermissionFromRole(roleId, permission);
        return ResponseEntity.noContent().build();
    }
}
