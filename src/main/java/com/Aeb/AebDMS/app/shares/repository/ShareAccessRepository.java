package com.Aeb.AebDMS.app.shares.repository;

import com.Aeb.AebDMS.app.shares.model.ShareAccess;
import com.Aeb.AebDMS.app.shares.model.ShareAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareAccessRepository extends JpaRepository<ShareAccess, ShareAccessId> {
    // Add custom query methods here
}
