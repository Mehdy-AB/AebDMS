package com.Aeb.AebDMS.app.favorites.service.impl;

import com.Aeb.AebDMS.app.favorites.model.Favorite;
import com.Aeb.AebDMS.app.favorites.service.IFavoriteService;
import com.Aeb.AebDMS.app.favorites.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements IFavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Override
    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }

    @Override
    public Page<Favorite> findAll(Pageable pageable) {
        return favoriteRepository.findAll(pageable);
    }

    @Override
    public Optional<Favorite> findById(Long id) {
     //   return favoriteRepository.findById(id);
        return null;
    }

    @Override
    public Favorite updateFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteById(Long id) {
        //favoriteRepository.deleteById(id);
    }
}
