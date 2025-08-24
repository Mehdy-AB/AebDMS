package com.Aeb.AebDMS.app.folders.model;

import com.Aeb.AebDMS.shared.util.GranteeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder_permissions")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FolderPermission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "grantee_type")
    private GranteeType granteeType;

    @Column(name = "grantee_id")
    private String granteeId;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "folderPermission")
    private List<FolderClosure> folderClosures = new ArrayList<>();

    //on folder
    @Column(name = "can_view")
    private boolean canView;
    @Column(name = "can_edit")
    private boolean canEdit;
    @Column(name = "can_create_sub_folders")
    private boolean canCreateSubFolders;
    @Column(name = "can_delete")
    private boolean canDelete;
    @Column(name = "can_share")
    private boolean canShare;
    @Column(name = "can_manage_permissions")
    private boolean canManagePermissions;
    //on sub doc
    @Column(name = "can_upload")
    private boolean canUpload;
    @Column(name = "can_edit_doc")
    private boolean canEditDoc;
    @Column(name = "can_delete_doc")
    private boolean canDeleteDoc;
    @Column(name = "can_share_doc")
    private boolean canShareDoc;
    @Column(name = "can_manage_permissions_doc")
    private boolean canManagePermissionsDoc;

    @Column(name = "inherits")
    private boolean inherits;

}

