package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Label;

public interface LabelService {

    Label find(final Long labelId);

    Label save(final Label label);

    void delete(final Long labelId);

}
