package com.Aeb.AebDMS.app.folders.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "folder_closure")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FolderPermissionId.class)
public class FolderClosure {
    @Id
    @Column(name = "folder_id")
    private Long folderId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", insertable = false, updatable = false)
    private Folder folder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private FolderPermission folderPermission;
}
