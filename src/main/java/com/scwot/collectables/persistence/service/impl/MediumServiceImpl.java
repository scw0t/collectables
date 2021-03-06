package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Medium;
import com.scwot.collectables.persistence.repository.MediumRepository;
import com.scwot.collectables.persistence.service.MediumService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MediumServiceImpl implements MediumService {

    private final MediumRepository mediumRepository;

    @Override
    public Medium find(final Long mediumId) {
        return mediumRepository.getOne(verifyNotNull(mediumId));
    }

    @Override
    public Medium save(final Medium medium) {
        return mediumRepository.saveAndFlush(verifyNotNull(medium));
    }

    @Override
    public void delete(final Long mediumId) {
        mediumRepository.deleteById(verifyNotNull(mediumId));
    }
}
