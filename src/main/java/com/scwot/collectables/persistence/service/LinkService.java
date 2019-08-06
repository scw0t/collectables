package com.scwot.collectables.persistence.service;

import com.scwot.collectables.enums.SupportedLinkType;
import com.scwot.collectables.persistence.model.Link;

import java.util.List;
import java.util.Set;

public interface LinkService {

    Link save(Link link);

    void remove(Long id, SupportedLinkType linkType);

    Set<Link> findByLinkIdAndType(Long id, SupportedLinkType linkType);

    List<Link> getAll();

}
