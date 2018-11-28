package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.enums.LinkType;

import java.util.List;

public interface LinkService {

    Link save(final Link link);

    void remove(final Long id, final LinkType linkType);

    List<Link> findByIdAndType(Long id, LinkType linkType);

    List<Link> getAll();

}
