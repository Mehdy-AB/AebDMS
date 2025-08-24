package com.Aeb.AebDMS.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String id ;
    String username;
    String firstName;
    String lastName;
    String[] jobTitle;
    String imageUrl;
    String email;
    Instant createdTimestamp;
}




