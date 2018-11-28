package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Release;
import com.scwot.collectables.persistence.repository.ReleaseRepository;
import com.scwot.collectables.persistence.service.ReleaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;

    @Override
    public Release save(final Release release) {
        return releaseRepository.saveAndFlush(verifyNotNull(release));
    }

    @Override
    public Release find(final Long releaseId) {
        return releaseRepository.findOne(verifyNotNull(releaseId));
    }

    @Override
    public void delete(final Long releaseId) {
        releaseRepository.delete(releaseId);
    }

}
