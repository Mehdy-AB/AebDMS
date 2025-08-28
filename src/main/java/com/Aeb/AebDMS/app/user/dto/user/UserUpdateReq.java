package com.Aeb.AebDMS.app.user.dto.user;

import lombok.Data;

@Data
public class UserUpdateReq {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
}