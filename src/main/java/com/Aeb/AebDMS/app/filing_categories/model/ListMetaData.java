package com.Aeb.AebDMS.app.filing_categories.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "list_metadata")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ListMetaData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;
    private String description;

    private boolean includeOtherValue =false;

    @OneToMany(mappedBy = "listMetaData",fetch = FetchType.EAGER, cascade = CascadeType.ALL ,orphanRemoval = true)
    List<ListMetadataOption> options;

    public void addOption(ListMetadataOption option) {
        option.setListMetaData(this); // set the parent
        if (this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.add(option);
    }
}
