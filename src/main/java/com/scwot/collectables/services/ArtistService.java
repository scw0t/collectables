package com.scwot.collectables.services;

import com.scwot.collectables.entities.Artist;

import java.util.List;

public interface ArtistService {

    Artist save(final Artist artist);

    void deleteByID(final Long id);

    List<Artist> findByName(final String name);

    Artist findById(final Long id);

    Artist findByMBID(final String mbid);

    List<Artist> getAll();

}
