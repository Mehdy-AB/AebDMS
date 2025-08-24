package com.Aeb.AebDMS.app.filing_categories.model;

import java.util.ArrayList;
import java.util.List;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "category_metadata_definitions")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataDefinition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    private MetadataType dataType;

    private boolean mandatory;

    @Column(name = "list_id",insertable = false, updatable = false)
    private Long listId;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id")
    private ListMetaData list;

    @Column(name = "category_id", insertable = false, updatable = false,nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private FilingCategory category;

    @OneToMany(mappedBy = "definition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Metadata> metadataEntries = new ArrayList<>();

}
