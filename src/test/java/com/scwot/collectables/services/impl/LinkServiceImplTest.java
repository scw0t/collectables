package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Link;
import com.scwot.collectables.enums.LinkResource;
import com.scwot.collectables.enums.LinkType;
import com.scwot.collectables.services.LinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LinkServiceImplTest {


    @Autowired
    private LinkService linkService;

    @Test
    public void save() throws Exception {
        assertThat(linkService.save(defaultLink()).getId(), is(1L));
    }

    @Test
    public void findByIdAndType() throws Exception {
        final Link link = defaultLink();
        linkService.save(link);

        final List<Link> byIdAndType =
                linkService.findByIdAndType(link.getEntityId(), link.getLinkType());

        assertThat(byIdAndType.size(), is(1));
    }

    @Test
    public void remove() throws Exception {
        final Link link = defaultLink();
        linkService.save(link);

        assertThat(linkService.findByIdAndType(link.getEntityId(), link.getLinkType()).size(),
                is(1));

        linkService.remove(link.getId(), link.getLinkType());

        assertThat(linkService.findByIdAndType(link.getEntityId(), link.getLinkType()).size(),
                is(0));
    }


    private Link defaultLink() {
        return Link.builder()
                .entityId(1L)
                .url("http:\\\\sample.com")
                .linkType(LinkType.ARTIST)
                .linkResource(LinkResource.MB)
                .build();
    }

}