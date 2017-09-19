package com.scwot.collectables.services;

import com.scwot.collectables.entities.Track;

public interface TrackService {

    Track find(final Long trackId);

    Track save(final Track track);

    void delete(final Long trackId);

}
