package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.AbstractTest;
import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.enums.SupportedLinkType;
import com.scwot.collectables.persistence.service.LinkService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinkServiceImplTest extends AbstractTest {

    @Autowired
    private LinkService linkService;

    @Test
    public void save() throws Exception {
        assertThat(linkService.save(defaultLink()).getLinkId(), is(1L));
    }

    @Test
    public void findByIdAndType() throws Exception {
        final Link link = defaultLink();
        linkService.save(link);

        final List<Link> byIdAndType =
                linkService.findByIdAndType(link.getLinkId(), link.getSupportedLinkType());

        assertThat(byIdAndType.size(), is(1));
    }

    @Test
    public void remove() throws Exception {
        final Link link = defaultLink();
        linkService.save(link);

        assertThat(linkService.findByIdAndType(link.getLinkId(), link.getSupportedLinkType()).size(),
                is(1));

        linkService.remove(link.getLinkId(), link.getSupportedLinkType());

        assertThat(linkService.findByIdAndType(link.getLinkId(), link.getSupportedLinkType()).size(),
                is(0));
    }


    private Link defaultLink() {
        return Link.builder()
                .url("http:\\\\sample.com")
                .supportedLinkType(SupportedLinkType.MUSICBRAINZ)
                .build();
    }

}