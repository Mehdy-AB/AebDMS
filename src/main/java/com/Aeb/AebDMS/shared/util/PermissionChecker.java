package com.Aeb.AebDMS.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionChecker {

    public boolean hasAllAuthorities(List<String> requiredAuthorities) {
        Authentication auth = getAuthentication();
        if (auth == null) return false;

        Set<String> userAuthorities = getAuthorityNames(auth.getAuthorities());

        return userAuthorities.containsAll(requiredAuthorities);
    }

    /**
     * Check if the current authenticated user has at least one of the given authorities.
     */
    public boolean hasAnyAuthority(List<String> requiredAuthorities) {
        Authentication auth = getAuthentication();
        if (auth == null) return false;

        Set<String> userAuthorities = getAuthorityNames(auth.getAuthorities());

        return requiredAuthorities.stream().anyMatch(userAuthorities::contains);
    }

    /**
     * Shortcut for single-authority check.
     */
    public boolean hasAuthority(String authority) {
        return hasAnyAuthority(List.of(authority));
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Set<String> getAuthorityNames(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}