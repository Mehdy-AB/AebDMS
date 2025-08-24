package com.Aeb.AebDMS.app.favorites.model;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.Aeb.AebDMS.app.documents.model.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;

@Entity
@Table(name = "favorites")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Favorite implements Serializable {

    @Id
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("documentId")  // Maps to documentId in composite key
    @JoinColumn(name = "document_id")
    private Document document;

    @Transient  // Helper field, not persisted
    public UUID getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public void setUserId(UUID userId) {
        if (id == null) id = new FavoriteId();
        id.setUserId(userId);
    }

    @CreationTimestamp
    private Instant createdAt;
}
