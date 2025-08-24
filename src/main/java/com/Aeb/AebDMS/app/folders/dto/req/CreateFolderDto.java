package com.Aeb.AebDMS.app.folders.dto.req;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateFolderDto {

    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    private Long parentId;

    @Valid
    private List<TypeShareAcces> usersGevenPermission = new ArrayList<>();

    @Valid
    private List<TypeShareAcces> goupesGevenPermission = new ArrayList<>();

    @Valid
    private List<TypeShareAcces> rolesGevenPermission = new ArrayList<>();

    @Valid
    private List<CreateFolderDto> subgroups  = new ArrayList<>();
}
