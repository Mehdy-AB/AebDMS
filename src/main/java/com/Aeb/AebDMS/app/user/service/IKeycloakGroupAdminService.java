package com.Aeb.AebDMS.app.user.service;


import com.Aeb.AebDMS.app.user.dto.GroupDto;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.group.GroupCreateReq;
import com.Aeb.AebDMS.app.user.dto.group.GroupUpdateReq;

import java.util.List;
import java.util.Optional;

public interface IKeycloakGroupAdminService {
    GroupDto createGroup(GroupCreateReq req);
    Optional<GroupDto> getGroupById(String groupId);
    List<GroupDto> getAllGroups(int first, int max);
    GroupDto updateGroup(String groupId, GroupUpdateReq req);
    void deleteGroup(String groupId);

    void addUserToGroup(String userId, String groupId);
    void removeUserFromGroup(String userId, String groupId);
    List<UserDto> getGroupMembers(String groupId, int first, int max);
}
