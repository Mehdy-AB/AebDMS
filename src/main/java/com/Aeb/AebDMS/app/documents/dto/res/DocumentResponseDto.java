package com.Aeb.AebDMS.app.documents.dto.res;

import com.Aeb.AebDMS.app.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class DocumentResponseDto {
    private Long documentId;
    private Long versionId;
    private UserDto createdBy;
    private UserDto ownedBy;
    private String name;
    private String path;
    private Long folderId;
    private Long sizeBytes;
    private String mimeType;
    private Long versionNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isPublic;
    private List<String> metadata;
}
