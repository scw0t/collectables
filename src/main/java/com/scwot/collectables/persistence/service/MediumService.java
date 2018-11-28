package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Medium;

public interface MediumService {

    Medium find(final Long mediumId);

    Medium save(final Medium medium);

    void delete(final Long mediumId);

}
