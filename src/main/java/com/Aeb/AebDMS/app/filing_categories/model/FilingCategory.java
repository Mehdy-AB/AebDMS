package com.Aeb.AebDMS.app.filing_categories.model;

import java.util.ArrayList;
import java.util.List;

import com.Aeb.AebDMS.app.link_rules.model.LinkRule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "filing_categories")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FilingCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "createdBy")
    private String createdBy;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<CategoryMetadataDefinition> metadataDefinitions = new ArrayList<>();

    @OneToMany(mappedBy = "sourceCategory", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<LinkRule> sourceLinkRules = new ArrayList<>();

    @OneToMany(mappedBy = "targetCategory", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<LinkRule> targetLinkRules = new ArrayList<>();
}
