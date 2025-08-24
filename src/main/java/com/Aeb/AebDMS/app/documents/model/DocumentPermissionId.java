package com.Aeb.AebDMS.app.documents.model;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)@NoArgsConstructor
@AllArgsConstructor
public class DocumentPermissionId implements Serializable {
    private Long documentId;
    private Long permissionId;
}