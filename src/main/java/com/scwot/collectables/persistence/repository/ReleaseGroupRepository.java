package com.scwot.collectables.persistence.repository;

import com.google.common.collect.Lists;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.Genre;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseGroupRepository extends JpaRepository<ReleaseGroup, Long> {

    List<Artist> getArtistListByReleaseGroupId(final Long releaseGroupId);

    List<Genre> getGenreListByReleaseGroupId(final Lists releaseGroupId);

}
