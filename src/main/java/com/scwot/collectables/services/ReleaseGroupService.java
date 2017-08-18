package com.scwot.collectables.services;

import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.entities.ReleaseGroup;

import java.util.List;

public interface ReleaseGroupService {

    ReleaseGroup findById(final Long releaseGroupId);

    List<Artist> getArtists(final Long releaseGroupId);

    ReleaseGroup save(final ReleaseGroup releaseGroup);

    void delete(final Long id);




}
