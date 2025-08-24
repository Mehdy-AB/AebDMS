package com.Aeb.AebDMS.app.link_rules.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.Aeb.AebDMS.app.documents.model.Document;

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
@Table(name = "document_links")
@Getter @Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class DocumentLink {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "from_document_id", insertable = false, updatable = false)
    private Long fromDocumentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_document_id")
    private Document source;

    @Column(name = "to_document_id", insertable = false, updatable = false)
    private Long toDocumentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_document_id")
    private Document target;

    @Column(name = "rule_id", insertable = false, updatable = false)
    private Long ruleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private LinkRule rule;

    private String linkType;
    private String createdBy;

    @CreationTimestamp
    private Instant createdAt;
}
