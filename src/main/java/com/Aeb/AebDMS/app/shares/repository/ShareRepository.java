package com.Aeb.AebDMS.app.shares.repository;

import com.Aeb.AebDMS.app.shares.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    // Add custom query methods here
}
