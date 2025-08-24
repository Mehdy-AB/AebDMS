package com.Aeb.AebDMS.app.filing_categories.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "list_metadata_option")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ListMetadataOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(name = "list_id", insertable = false, updatable = false)
    private Long listId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id",nullable = false)
    private ListMetaData listMetaData;
}
