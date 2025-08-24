package com.Aeb.AebDMS.app.link_rules.service.impl;

import com.Aeb.AebDMS.app.link_rules.model.LinkRule;
import com.Aeb.AebDMS.app.link_rules.service.ILinkRuleService;
import com.Aeb.AebDMS.app.link_rules.repository.LinkRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkRuleServiceImpl implements ILinkRuleService {

    private final LinkRuleRepository linkruleRepository;

    @Override
    public LinkRule saveLinkRule(LinkRule linkrule) {
        return linkruleRepository.save(linkrule);
    }

    @Override
    public List<LinkRule> findAll() {
        return linkruleRepository.findAll();
    }

    @Override
    public Page<LinkRule> findAll(Pageable pageable) {
        return linkruleRepository.findAll(pageable);
    }

    @Override
    public Optional<LinkRule> findById(Long id) {
        return linkruleRepository.findById(id);
    }

    @Override
    public LinkRule updateLinkRule(LinkRule linkrule) {
        return linkruleRepository.save(linkrule);
    }

    @Override
    public void deleteById(Long id) {
        linkruleRepository.deleteById(id);
    }
}
