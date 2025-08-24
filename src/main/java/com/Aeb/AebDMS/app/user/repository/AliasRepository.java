package com.Aeb.AebDMS.app.user.repository;

import com.Aeb.AebDMS.app.user.model.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AliasRepository extends JpaRepository<Alias, Long> {
    // Add custom query methods here
}
