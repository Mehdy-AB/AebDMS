package com.Aeb.AebDMS.app.shares.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "share_access")
@IdClass(ShareAccessId.class)
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShareAccess {
    @Id
    @Column(name = "share_id")
    private Long shareId;

    @Id
    @Column(name = "grantee_type", length = 10)
    private String granteeType;

    @Id
    @Column(name = "grantee_id_or_email")
    private String granteeIdOrEmail;

    @Column(name = "can_view")
    private boolean canView;

    @Column(name = "can_download")
    private boolean canDownload;

    @Column(name = "can_edit")
    private boolean canEdit;
}
