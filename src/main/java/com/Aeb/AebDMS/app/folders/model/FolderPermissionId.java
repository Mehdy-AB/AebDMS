package com.Aeb.AebDMS.app.folders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)@NoArgsConstructor
@AllArgsConstructor
public class FolderPermissionId implements Serializable {
    private Long folderId;
    private Long permissionId;
}