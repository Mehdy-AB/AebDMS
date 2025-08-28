package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.dto.app.CustomUserRepresentation;
import com.Aeb.AebDMS.app.user.dto.user.UserCreateReq;
import com.Aeb.AebDMS.app.user.dto.user.UserUpdateReq;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserAdminService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakUserAdminServiceImpl implements IKeycloakUserAdminService {
    private final RealmResource realm;

    @Override
    public UserDto createUser(UserCreateReq req) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEnabled(true);

        var response = realm.users().create(user);
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        if (req.getPassword() != null) {
            CredentialRepresentation password = new CredentialRepresentation();
            password.setTemporary(false);
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(req.getPassword());

            realm.users().get(userId).resetPassword(password);
        }

        return getUserById(userId).orElseThrow();
    }

    @Override
    public Optional<UserDto> getUserById(String userId) {
        try {
            UserRepresentation user = realm.users().get(userId).toRepresentation();
            return Optional.of(new CustomUserRepresentation(user).getUser());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserDto> getAllUsers(int first, int max) {
        return realm.users().list(first, max)
                .stream().map(user->new CustomUserRepresentation(user).getUser()).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String userId, UserUpdateReq req) {
        UserResource userResource = realm.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getEnabled() != null) user.setEnabled(req.getEnabled());

        userResource.update(user);
        return new CustomUserRepresentation(userResource.toRepresentation()).getUser();
    }

    @Override
    public void deleteUser(String userId) {
        realm.users().get(userId).remove();
    }

}
