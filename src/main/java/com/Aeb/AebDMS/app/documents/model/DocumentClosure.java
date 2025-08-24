package com.Aeb.AebDMS.app.documents.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "document_closure")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DocumentPermissionId.class)
public class DocumentClosure {
    @Id
    @Column(name = "document_id")
    private Long documentId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private DocumentPermission documentPermission;

    public DocumentClosure(long documentId, Long id) {
    }
}
