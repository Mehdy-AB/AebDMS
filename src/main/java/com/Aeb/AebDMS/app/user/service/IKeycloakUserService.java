package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.RoleDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IKeycloakUserService {
    public Optional<UserDto> getUserById(String userIds);

    //public List<UserRepresentation> getUsersByIds(List<String> userIds);

    public List<Boolean> getUsersExistingByIds(List<String> userIds);

    public Optional<RoleDto> getRoleById(String roleIds);

    //public List<RoleDto> getRolesByIds(List<String> roleIds);

    public List<Boolean> getRolesExistingByIds(List<String> roleIds);

    public Optional<GroupDto> getGroupById(String groupId);

    public List<GroupRepresentation> getGroupesByIds(List<String> groupIds);

    public List<Boolean> getGroupersExistingByIds(List<String> groupIds);

    public List<String> getAllUserGroupAndRolesId(String userId);

    Map<String, UserDto> getUsersByIds(Collection<String> ids);

    Map<String, GroupDto> getGroupsByIds(Collection<String> ids);

    Map<String, RoleDto> getRolesByIds(Collection<String> ids);
}
