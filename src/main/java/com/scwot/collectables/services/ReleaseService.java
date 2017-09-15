package com.scwot.collectables.services;

import com.scwot.collectables.entities.Release;

public interface ReleaseService {

    Release find(final Long releaseId);

    Release save(final Release release);

    void delete(final Long releaseId);

}
