package com.Aeb.AebDMS.app.user.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupCreateReq {
    @NotBlank
    private String name;
    private String path; // optional
}