package com.scwot.collectables.services;

import com.scwot.collectables.entities.Medium;

public interface MediumService {

    Medium find(final Long mediumId);

    Medium save(final Medium medium);

    void delete(final Long mediumId);

}
