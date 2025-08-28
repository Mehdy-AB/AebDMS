package com.Aeb.AebDMS.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    String id ;
    String name;
    String description;
    private Set<String> permissions;

}





