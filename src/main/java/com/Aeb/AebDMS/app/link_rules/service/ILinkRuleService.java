package com.Aeb.AebDMS.app.link_rules.service;

import com.Aeb.AebDMS.app.link_rules.model.LinkRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ILinkRuleService {
    
    LinkRule saveLinkRule(LinkRule linkrule);
    
    List<LinkRule> findAll();
    
    Page<LinkRule> findAll(Pageable pageable);
    
    Optional<LinkRule> findById(Long id);
    
    LinkRule updateLinkRule(LinkRule linkrule);
    
    void deleteById(Long id);
}
