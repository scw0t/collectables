package com.scwot.collectables.services;

import com.scwot.collectables.entities.Medium;
import com.scwot.collectables.entities.Release;

import java.util.List;

public interface ReleaseService {

    Release find(final Long releaseId);

    Release save(final Release release);

    void delete(final Long releaseId);

    List<Medium> getMediums(final Long releaseId);

}
