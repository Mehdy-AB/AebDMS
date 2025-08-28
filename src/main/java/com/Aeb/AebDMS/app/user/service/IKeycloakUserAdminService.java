package com.Aeb.AebDMS.app.user.service;


import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.user.UserCreateReq;
import com.Aeb.AebDMS.app.user.dto.user.UserUpdateReq;

import java.util.List;
import java.util.Optional;

public interface IKeycloakUserAdminService {
    UserDto createUser(UserCreateReq req);
    Optional<UserDto> getUserById(String userId);
    List<UserDto> getAllUsers(int first, int max);
    UserDto updateUser(String userId, UserUpdateReq req);
    void deleteUser(String userId);
}