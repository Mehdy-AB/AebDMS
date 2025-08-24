package com.Aeb.AebDMS.app.documents.model;

import com.Aeb.AebDMS.shared.util.GranteeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "document_permissions")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class DocumentPermission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "grantee_type")
    private GranteeType granteeType;

    @Column(name = "grantee_id")
    private String granteeId;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "documentPermission")
    private List<DocumentClosure> documentClosures;

    //on folder
    @Column(name = "can_view")
    private boolean canView;
    @Column(name = "can_edit")
    private boolean canEdit;

    @Column(name = "can_delete")
    private boolean canDelete;

    @Column(name = "can_manage_permissions")
    private boolean canManagePermissions;


}

