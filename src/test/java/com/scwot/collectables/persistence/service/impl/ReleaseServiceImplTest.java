package com.scwot.collectables.persistence.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.scwot.collectables.AbstractTest;
import com.scwot.collectables.persistence.model.Medium;
import com.scwot.collectables.persistence.model.Release;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import com.scwot.collectables.persistence.service.ReleaseGroupService;
import com.scwot.collectables.persistence.service.ReleaseService;

import javax.transaction.Transactional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReleaseServiceImplTest extends AbstractTest {

    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private ReleaseGroupService releaseGroupService;

    @Test
    @Transactional
    public void saveAndFind() throws Exception {
        final Release release = releaseService.save(defaultRelease("release1"));
        assertThat(release, is(notNullValue()));
        assertThat(release.getReleaseId(), is(1L));

        final Release found = releaseService.find(release.getReleaseId());
        assertThat(release.getName(), is(equalTo(found.getName())));

        ReleaseGroup releaseGroup = releaseGroupService.save(defaultRg());
        releaseGroup.setReleaseList(Lists.newArrayList(release));
        releaseGroup = releaseGroupService.save(releaseGroup);

        assertThat(releaseGroup.getReleaseList().size(), is(1));
    }

    @Transactional
    @Test
    public void saveToReleaseGroupThenDelete() {
        ReleaseGroup releaseGroup = releaseGroupService.save(defaultRg());
        releaseGroup = releaseGroupService.findById(releaseGroup.getReleaseGroupId());

        Release release = defaultRelease("release1");
        release = releaseGroupService.saveRelease(releaseGroup.getReleaseGroupId(), release);

        assertThat(releaseGroup.getReleaseList().size(), is(1));
        assertEquals(release, releaseGroup.getReleaseList().get(0));

        releaseGroupService.deleteRelease(releaseGroup.getReleaseGroupId(), release);
        releaseGroup = releaseGroupService.findById(releaseGroup.getReleaseGroupId());

        assertThat(releaseGroup.getReleaseList().size(), is(0));
    }


    @Transactional
    @Test
    public void getMediums() throws Exception {
        ReleaseGroup releaseGroup = defaultRg();
        Release release = defaultRelease("release");
        final Medium medium = defaultMedium();
        release.setMediumList(Lists.newArrayList(medium));
        releaseGroup.setReleaseList(Lists.newArrayList(release));
        releaseGroup = releaseGroupService.save(releaseGroup);

        release = releaseService.find(releaseGroup.getReleaseList().get(0).getReleaseId());
        assertThat(release.getMediumList().size(), is(1));
    }

    private Medium defaultMedium() {
        return Medium.builder()
                .name("medium")
                .build();
    }

    private Release defaultRelease(final String name) {
        return Release.builder()
                .name(name)
                .build();
    }

    private ReleaseGroup defaultRg() {
        return ReleaseGroup.builder()
                .name("releaseGroup1")
                .build();
    }

}