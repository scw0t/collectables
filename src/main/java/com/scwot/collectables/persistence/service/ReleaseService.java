package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Release;

public interface ReleaseService {

    Release find(final Long releaseId);

    Release save(final Release release);

    void delete(final Long releaseId);

}
