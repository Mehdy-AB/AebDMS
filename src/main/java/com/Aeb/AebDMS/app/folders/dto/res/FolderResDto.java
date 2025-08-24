package com.Aeb.AebDMS.app.folders.dto.res;


import com.Aeb.AebDMS.app.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FolderResDto {
    private Long id;

    private String name;

    private String description;

    private Long parentId;

    private UserDto createdBy;

    private UserDto ownedBy;

    private boolean isPublic;

    private Long size;

    private Instant createdAt;

    private Instant updatedAt;

    private String path;


}
