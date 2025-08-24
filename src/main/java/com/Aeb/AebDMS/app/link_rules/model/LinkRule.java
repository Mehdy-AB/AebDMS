package com.Aeb.AebDMS.app.link_rules.model;

import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "link_rules")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class LinkRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "source_category_id", insertable = false, updatable = false)
    private Long sourceCategoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_category_id")
    private FilingCategory sourceCategory;

    @Column(name = "target_category_id", insertable = false, updatable = false)
    private Long targetCategoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_category_id")
    private FilingCategory targetCategory;

    @Column(columnDefinition = "JSONB")
    private String metadataKeys;
    private String operator;
    private boolean automatic;
}
