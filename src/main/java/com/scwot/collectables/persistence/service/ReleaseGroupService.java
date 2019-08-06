package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.ReleaseGroup;

import java.util.List;

public interface ReleaseGroupService {

    ReleaseGroup findById(final Long releaseGroupId);

    List<Artist> getArtists(final Long releaseGroupId);

    ReleaseGroup save(final ReleaseGroup releaseGroup);

    void delete(final Long id);

}
