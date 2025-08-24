package com.Aeb.AebDMS.app.favorites.service;

import com.Aeb.AebDMS.app.favorites.model.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IFavoriteService {
    
    Favorite saveFavorite(Favorite favorite);
    
    List<Favorite> findAll();
    
    Page<Favorite> findAll(Pageable pageable);
    
    Optional<Favorite> findById(Long id);
    
    Favorite updateFavorite(Favorite favorite);
    
    void deleteById(Long id);
}
