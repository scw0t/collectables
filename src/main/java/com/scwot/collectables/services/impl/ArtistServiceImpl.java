package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.repository.ArtistRepository;
import com.scwot.collectables.services.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public Artist save(Artist artist) {
        return artistRepository.save(verifyNotNull(artist));
    }

    @Override
    public void deleteByID(Long id) {
        artistRepository.delete(verifyNotNull(id));
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findOne(verifyNotNull(id));
    }

    @Override
    public List<Artist> findByName(String name) {
        return artistRepository.findByName(verifyNotNull(name));
    }

    @Override
    public Artist findByMBID(String mbid) {
        return artistRepository.findByMbid(verifyNotNull(mbid));
    }

    @Override
    public List<Artist> getAll() {
        return artistRepository.findAll();
    }
}
