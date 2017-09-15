package com.scwot.collectables.services.impl;

import com.google.common.collect.Lists;
import com.scwot.collectables.AbstractTest;
import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.entities.ReleaseGroup;
import com.scwot.collectables.enums.ReleaseType;
import com.scwot.collectables.services.ArtistService;
import com.scwot.collectables.services.ReleaseGroupService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReleaseGroupServiceImplTest extends AbstractTest{

    @Autowired
    private ReleaseGroupService releaseGroupService;

    @Autowired
    private ArtistService artistService;

    @Test
    public void save() throws Exception {
        final ReleaseGroup rg1 = ReleaseGroup.builder()
                .name("Test release group")
                .mbid("rg123")
                .type(ReleaseType.ALBUM)
                .build();

        assertThat(releaseGroupService.save(rg1).getReleaseGroupId(), is(1L));
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
        rg.setArtistList(Lists.newArrayList(artist));

        rg = releaseGroupService.save(rg);
        assertThat(rg.getReleaseGroupId(), is(1L));
        assertThat(rg.getArtistList().get(0).getArtistId(), is(artist.getArtistId()));

        artist.setReleaseGroupList(Lists.newArrayList(rg));
        artist = artistService.save(artist);
        assertThat(artist.getReleaseGroupList().size(), is(1));
    }

    private ReleaseGroup defaultReleaseGroup() {
        return ReleaseGroup.builder()
                .name("Test release group")
                .mbid("rg123")
                .type(ReleaseType.ALBUM)
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
        rg.setArtistList(Lists.newArrayList(artist1, artist2));
        rg = releaseGroupService.save(rg);

        artist1.setReleaseGroupList(Lists.newArrayList(rg));
        artist2.setReleaseGroupList(Lists.newArrayList(rg));

        artist1 = artistService.save(artist1);
        artist2 = artistService.save(artist2);

        assertThat(rg.getArtistList().size(), is(2));
        assertThat(artist1.getReleaseGroupList().get(0).getReleaseGroupId(), is(rg.getReleaseGroupId()));
        assertThat(artist2.getReleaseGroupList().get(0).getReleaseGroupId(), is(rg.getReleaseGroupId()));
    }

    @Test
    public void delete() throws Exception {
        final ReleaseGroup savedRG = releaseGroupService.save(defaultReleaseGroup());
        assertThat(savedRG.getReleaseGroupId(), is(1L));

        releaseGroupService.delete(savedRG.getReleaseGroupId());

        assertThat(releaseGroupService.findById(savedRG.getReleaseGroupId()), is(nullValue()));
    }

}