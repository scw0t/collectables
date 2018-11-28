package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Label;
import com.scwot.collectables.persistence.repository.LabelRepository;
import com.scwot.collectables.persistence.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label find(final Long labelId) {
        return labelRepository.findOne(verifyNotNull(labelId));
    }

    @Override
    public Label save(final Label label) {
        return labelRepository.saveAndFlush(verifyNotNull(label));
    }

    @Override
    public void delete(final Long labelId) {
        labelRepository.delete(verifyNotNull(labelId));
    }
}
