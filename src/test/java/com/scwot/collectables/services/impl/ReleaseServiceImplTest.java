package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Release;
import com.scwot.collectables.entities.ReleaseGroup;
import com.scwot.collectables.services.ReleaseGroupService;
import com.scwot.collectables.services.ReleaseService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReleaseServiceImplTest {

    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private ReleaseGroupService releaseGroupService;

    @Test
    @Transactional
    public void saveAndFind() throws Exception {
        ReleaseGroup releaseGroup = releaseGroupService.save(defaultRG());

        final Release release = releaseService.save(defaultRelease("release1"));
        assertThat(release, is(notNullValue()));
        assertThat(release.getReleaseId(), is(1L));

        final Release found = releaseService.find(release.getReleaseId());
        assertThat(release.getName(), is(equalTo(found.getName())));

        releaseGroup.setReleaseList(Lists.newArrayList(release));
        releaseGroup = releaseGroupService.save(releaseGroup);

        assertThat(releaseGroup.getReleaseList().size(), is(1));
    }

    private Release defaultRelease(final String name) {
        return Release.builder()
                .name(name)
                .build();
    }

    private ReleaseGroup defaultRG() {
        return ReleaseGroup.builder()
                .name("releaseGroup1")
                .build();
    }

}