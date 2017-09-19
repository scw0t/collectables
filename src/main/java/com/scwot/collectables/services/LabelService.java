package com.scwot.collectables.services;

import com.scwot.collectables.entities.Label;

public interface LabelService {

    Label find(final Long labelId);

    Label save(final Label label);

    void delete(final Long labelId);

}
