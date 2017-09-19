package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Medium;
import com.scwot.collectables.repository.MediumRepository;
import com.scwot.collectables.services.MediumService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MediumServiceImpl implements MediumService {

    private final MediumRepository mediumRepository;

    @Override
    public Medium find(final Long mediumId) {
        return mediumRepository.findOne(verifyNotNull(mediumId));
    }

    @Override
    public Medium save(final Medium medium) {
        return mediumRepository.saveAndFlush(verifyNotNull(medium));
    }

    @Override
    public void delete(final Long mediumId) {
        mediumRepository.delete(verifyNotNull(mediumId));
    }
}
