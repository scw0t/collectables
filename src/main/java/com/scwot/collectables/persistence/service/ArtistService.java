package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Artist;

import java.util.List;

public interface ArtistService {

    Artist save(final Artist artist);

    void delete(final Long id);

    List<Artist> findByName(final String name);

    Artist findById(final Long id);

    Artist findByMbId(final String mbid);

    List<Artist> getAll();

}
