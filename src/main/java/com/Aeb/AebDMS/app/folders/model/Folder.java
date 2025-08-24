package com.Aeb.AebDMS.app.folders.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "folders")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Folder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 2000)
    private String path;

    @PrePersist @PreUpdate
    private void updatePath() {
        if (parent != null) {
            this.path = parent.getPath() + "/" + name;
        } else {
            this.path = ownedBy + "/" + name;
        }
    }

    @ToString.Include
    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Scalar FK for parent
    @Column(name = "parent_id",insertable = false,updatable = false)
    private Long parentId;

    // Parent folder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",nullable = true)
    private Folder parent;

    // Sub-folders
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subFolders = new ArrayList<>();

    @Column(name = "created_by", nullable = false, length = 36)
    private String createdBy;

    @Column(name = "owned_by", nullable = false, length = 36)
    private String ownedBy;

    @OneToMany(mappedBy = "folder",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<FolderClosure> folderClosures = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "isPublic",nullable = false)
    private boolean isPublic=false;

}
