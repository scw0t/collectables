package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Track;

public interface TrackService {

    Track find(final Long trackId);

    Track save(final Track track);

    void delete(final Long trackId);

}
