package com.Aeb.AebDMS.app.favorites.model;

import com.Aeb.AebDMS.app.documents.model.Document;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
public class FavoriteId implements Serializable {
    @Column(name = "user_id", length = 36)
    private UUID userId;

    @Column(name = "document_id")
    private Long documentId;  // Must match Document's ID type

    // Optional constructor if needed
    public FavoriteId(UUID userId, Document document) {
        this.userId = userId;
        this.documentId = document.getId();
    }
}