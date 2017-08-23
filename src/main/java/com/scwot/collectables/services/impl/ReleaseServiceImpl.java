package com.scwot.collectables.services.impl;

import com.google.common.base.Verify;
import com.scwot.collectables.entities.Medium;
import com.scwot.collectables.entities.Release;
import com.scwot.collectables.repository.ReleaseRepository;
import com.scwot.collectables.services.ReleaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Verify.*;

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

    }

    @Override
    public List<Medium> getMediums(final Long releaseId) {
        return null;
    }
}
