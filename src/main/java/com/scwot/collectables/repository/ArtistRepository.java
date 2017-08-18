package com.scwot.collectables.repository;

import com.scwot.collectables.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByName(final String name);

    Artist findByMbid(final String mbid);

}
