package com.scwot.collectables.services.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

import com.google.common.base.VerifyException;
import com.google.common.collect.Lists;
import com.scwot.collectables.AbstractTest;
import com.scwot.collectables.entities.Artist;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArtistServiceImplTest extends AbstractTest {

    private static final String ARTIST_NAME = "artist";
    private static final String ARTIST_MBID = "123";

    @Autowired
    private ArtistServiceImpl artistService;

    @Test
    public void save() throws Exception {
        assertThat(artistService.save(defaultArtist()).getArtistId(), is(1L));
    }

    @Test
    public void saveWithPhoto() throws Exception {
        URL url = getClass().getResource("/default/default-photo.jpg");
        File file = new File(url.getPath());
        byte[] fileBytes = new byte[(int) file.length()];

        assertThat(fileBytes, is(notNullValue()));

        try (final FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBytes);
        }

        Artist artist = defaultArtist();
        artist.setImage(fileBytes);
        artistService.save(artist);

        artist = artistService.findById(1L);
        assertThat(artist.getImage(), is(notNullValue()));
        assertThat(artist.getImage().length, is(greaterThan(0)));
    }

    @Test
    @Transactional
    public void saveMembers() throws Exception {
        Artist group = defaultArtist();
        final Long artistId = artistService.save(group).getArtistId();

        Artist member1 = Artist.builder()
                .name("member1")
                .sortName("member1")
                .build();

        Artist member2 = Artist.builder()
                .name("member2")
                .sortName("member2")
                .build();

        final Long member1Id = artistService.save(member1).getArtistId();
        final Long member2Id = artistService.save(member2).getArtistId();

        group = artistService.findById(artistId);
        group.setMembers(Lists.newArrayList(member1, member2));
        artistService.save(group);

        final Artist saved = artistService.findById(artistId);
        assertThat(saved.getMembers().size(), is(2));

        member1 = artistService.findById(member1Id);
        member1.setMemberOf(Lists.newArrayList(group));
        artistService.save(member1);

        member1 = artistService.findById(member1Id);

        assertThat(member1, is(notNullValue()));
        assertThat(member1.getMemberOf().size(), is(1));
        assertThat(member1.getMemberOf().get(0).getArtistId(), is(artistId));

        member2 = artistService.findById(member2Id);
        member2.setMemberOf(Lists.newArrayList(group));
        artistService.save(member2);
        assertThat(member2, is(notNullValue()));
        assertThat(member2.getMemberOf().size(), is(1));
        assertThat(member2.getMemberOf().get(0).getArtistId(), is(artistId));
    }

    @Test(expected = VerifyException.class)
    public void saveNull() {
        artistService.save(null);
    }

    @Test
    public void deleteById() throws Exception {
        final Long id = artistService.save(defaultArtist()).getArtistId();

        assertThat(artistService.findById(id).getArtistId(), is(id));

        artistService.delete(id);

        assertThat(artistService.findById(id), is(nullValue()));
    }

    @Test
    public void findByName() throws Exception {
        artistService.save(defaultArtist());
        final List<Artist> artistList = artistService.findByName(ARTIST_NAME);
        assertTrue(artistList.stream()
                .map(Artist::getName)
                .anyMatch(name -> name.equals(ARTIST_NAME)));
    }

    @Test
    public void findByMbId() throws Exception {
        artistService.save(defaultArtist());
        final Artist artistByMbId = artistService.findByMbId(ARTIST_MBID);
        assertThat(artistByMbId.getMbid(), is(equalTo(ARTIST_MBID)));
    }

    @Test
    public void getAll() throws Exception {
        final Artist anotherArtist = Artist.builder()
                .name("another_artist")
                .sortName("another_artist")
                .mbid("333")
                .build();
        artistService.save(defaultArtist());
        artistService.save(anotherArtist);

        final List<Artist> artistList = artistService.getAll();
        assertThat(artistList.size(), is(2));
    }

    private Artist defaultArtist() {
        return Artist.builder()
                .mbid(ARTIST_MBID)
                .name(ARTIST_NAME)
                .sortName(ARTIST_NAME)
                .build();
    }


}