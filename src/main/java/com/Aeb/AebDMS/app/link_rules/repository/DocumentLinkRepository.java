package com.Aeb.AebDMS.app.link_rules.repository;

import com.Aeb.AebDMS.app.link_rules.model.DocumentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLinkRepository extends JpaRepository<DocumentLink, Long> {
    // Add custom query methods here
}
