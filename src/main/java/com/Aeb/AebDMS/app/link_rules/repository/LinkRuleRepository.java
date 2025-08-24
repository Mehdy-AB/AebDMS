package com.Aeb.AebDMS.app.link_rules.repository;

import com.Aeb.AebDMS.app.link_rules.model.LinkRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRuleRepository extends JpaRepository<LinkRule, Long> {
    // Add custom query methods here
}
