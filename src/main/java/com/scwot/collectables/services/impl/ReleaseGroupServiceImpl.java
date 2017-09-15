package com.scwot.collectables.services.impl;

import com.google.common.collect.Lists;
import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.entities.Release;
import com.scwot.collectables.entities.ReleaseGroup;
import com.scwot.collectables.repository.ReleaseGroupRepository;
import com.scwot.collectables.repository.ReleaseRepository;
import com.scwot.collectables.services.ReleaseGroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReleaseGroupServiceImpl implements ReleaseGroupService {

    private ReleaseGroupRepository releaseGroupRepository;
    private ReleaseRepository releaseRepository;

    @Override
    public ReleaseGroup save(final ReleaseGroup releaseGroup) {
        return releaseGroupRepository.saveAndFlush(verifyNotNull(releaseGroup));
    }

    @Override
    public Release saveRelease(final Long releaseGroupId, final Release release) {
        final ReleaseGroup releaseGroup = findById(releaseGroupId);
        if (releaseGroup.getReleaseList() == null) {
            releaseGroup.setReleaseList(Lists.newArrayList());
        }
        releaseGroup.getReleaseList().add(release);
        final List<Release> releaseList = save(releaseGroup).getReleaseList();
        return releaseList.get(releaseList.size() - 1);
    }

    @Override
    public void deleteRelease(final Long releaseGroupId, final Release release) {
        final ReleaseGroup releaseGroup = findById(releaseGroupId);
        if (releaseGroup.getReleaseList() != null) {
            releaseGroup.getReleaseList().remove(release);
        }
    }

    @Override
    public ReleaseGroup findById(Long releaseGroupId) {
        return releaseGroupRepository.findOne(verifyNotNull(releaseGroupId));
    }

    @Override
    public List<Artist> getArtists(final Long releaseGroupId) {
        return releaseGroupRepository.getArtistListByReleaseGroupId(verifyNotNull(releaseGroupId));
    }

    @Override
    public void delete(Long id) {
        releaseGroupRepository.delete(id);
    }
}
