package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.enums.LinkType;
import com.scwot.collectables.persistence.repository.LinkRepository;
import com.scwot.collectables.persistence.service.LinkService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public Link save(Link link) {
        return linkRepository.save(verifyNotNull(link));
    }

    @Transactional
    @Override
    public void remove(Long id, LinkType linkType) {
        linkRepository.deleteByIdAndLinkType(verifyNotNull(id), linkType);
    }

    @Override
    public List<Link> findByIdAndType(Long id, LinkType linkType) {
        return linkRepository.findByIdAndLinkType(id, linkType);
    }

    @Override
    public List<Link> getAll() {
        return linkRepository.findAll();
    }
}
