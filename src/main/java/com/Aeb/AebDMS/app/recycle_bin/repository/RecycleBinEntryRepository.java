package com.Aeb.AebDMS.app.recycle_bin.repository;

import com.Aeb.AebDMS.app.recycle_bin.model.RecycleBinEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecycleBinEntryRepository extends JpaRepository<RecycleBinEntry, Long> {
    // Add custom query methods here
}
