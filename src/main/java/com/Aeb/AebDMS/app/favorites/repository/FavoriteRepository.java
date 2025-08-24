package com.Aeb.AebDMS.app.favorites.repository;

import com.Aeb.AebDMS.app.favorites.model.Favorite;


import com.Aeb.AebDMS.app.favorites.model.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    // Add custom query methods here
}
