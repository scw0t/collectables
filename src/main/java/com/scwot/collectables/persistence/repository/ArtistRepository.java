package com.scwot.collectables.persistence.repository;

import com.scwot.collectables.persistence.model.Artist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByName(final String name);

    Artist findByMbid(final String mbid);

}
