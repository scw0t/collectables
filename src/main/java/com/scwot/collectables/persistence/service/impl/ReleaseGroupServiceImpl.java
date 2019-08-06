package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import com.scwot.collectables.persistence.repository.ReleaseGroupRepository;
import com.scwot.collectables.persistence.service.ReleaseGroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReleaseGroupServiceImpl implements ReleaseGroupService {

    private ReleaseGroupRepository releaseGroupRepository;

    @Override
    public ReleaseGroup save(final ReleaseGroup releaseGroup) {
        return releaseGroupRepository.save(verifyNotNull(releaseGroup));
    }

    @Override
    public ReleaseGroup findById(Long releaseGroupId) {
        return releaseGroupRepository.getOne(verifyNotNull(releaseGroupId));
    }

    @Override
    public List<Artist> getArtists(final Long releaseGroupId) {
        return releaseGroupRepository.getArtistListByReleaseGroupId(verifyNotNull(releaseGroupId));
    }

    @Override
    public void delete(Long id) {
        releaseGroupRepository.deleteById(id);
    }
}
