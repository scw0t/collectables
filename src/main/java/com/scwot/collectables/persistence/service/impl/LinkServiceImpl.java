package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.enums.SupportedLinkType;
import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.persistence.repository.LinkRepository;
import com.scwot.collectables.persistence.service.LinkService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public Link save(Link link) {
        return linkRepository.save(verifyNotNull(link));
    }

    @Override
    public void remove(Long id, SupportedLinkType type) {
        linkRepository.deleteByLinkIdAndSupportedLinkType(verifyNotNull(id), type);
    }

    @Override
    public Set<Link> findByLinkIdAndType(Long id, SupportedLinkType type) {
        return linkRepository.findByLinkIdAndSupportedLinkType(id, type);
    }

    @Override
    public List<Link> getAll() {
        return linkRepository.findAll();
    }
}
