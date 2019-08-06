package com.scwot.collectables.persistence.service.impl;

import com.google.common.collect.Sets;
import com.scwot.collectables.AbstractTest;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import com.scwot.collectables.persistence.service.ArtistService;
import com.scwot.collectables.persistence.service.ReleaseGroupService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReleaseGroupServiceImplTest extends AbstractTest {

    @Autowired
    private ReleaseGroupService releaseGroupService;

    @Autowired
    private ArtistService artistService;

    @Test
    public void save() throws Exception {
        /*final ReleaseGroup rg1 = ReleaseGroup.builder()
                .name("Test release group")
                .mbid("rg123")
                .type(ReleaseType.ALBUM)
                .build();

        assertThat(releaseGroupService.save(rg1).getReleaseGroupId(), is(1L));*/
    }

    @Transactional
    @Test
    public void saveWithArtist() throws Exception {
        Artist artist = Artist.builder()
                .name("artist")
                .sortName("artist")
                .mbid("333")
                .build();

        artist = artistService.save(artist);

        ReleaseGroup rg = defaultReleaseGroup();
        rg.setArtists(Sets.newHashSet(artist));

        rg = releaseGroupService.save(rg);
        assertThat(rg.getReleaseGroupId(), is(1L));

        artist.setReleaseGroups(Sets.newHashSet(rg));
        artist = artistService.save(artist);
    }

    private ReleaseGroup defaultReleaseGroup() {
        return ReleaseGroup.builder()
                .name("Test release group")
                .mbid("rg123")
                //.type(ReleaseType.ALBUM)
                .build();
    }

    @Transactional
    @Test
    public void getArtists() throws Exception {
        Artist artist1 = Artist.builder()
                .name("artist1")
                .sortName("artist1")
                .mbid("111")
                .build();

        Artist artist2 = Artist.builder()
                .name("artist2")
                .sortName("artist2")
                .mbid("222")
                .build();

        artist1 = artistService.save(artist1);
        artist2 = artistService.save(artist2);

        ReleaseGroup rg = defaultReleaseGroup();
        rg.setArtists(Sets.newHashSet(artist1, artist2));
        rg = releaseGroupService.save(rg);

        artist1.setReleaseGroups(Sets.newHashSet(rg));
        artist2.setReleaseGroups(Sets.newHashSet(rg));

        artist1 = artistService.save(artist1);
        artist2 = artistService.save(artist2);

        assertThat(rg.getArtists().size(), is(2));
    }

    @Test
    public void delete() throws Exception {
        final ReleaseGroup savedRg = releaseGroupService.save(defaultReleaseGroup());
        assertThat(savedRg.getReleaseGroupId(), is(1L));

        releaseGroupService.delete(savedRg.getReleaseGroupId());

        assertThat(releaseGroupService.findById(savedRg.getReleaseGroupId()), is(nullValue()));
    }

}