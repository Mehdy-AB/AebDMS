package com.Aeb.AebDMS.app.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateReq {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String password; // optional, can be set later
}