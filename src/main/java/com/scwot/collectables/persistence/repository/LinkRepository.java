package com.scwot.collectables.persistence.repository;

import com.scwot.collectables.enums.SupportedLinkType;
import com.scwot.collectables.persistence.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    void deleteByLinkIdAndType(@Param("link_id") final Long linkId,
                               @Param("type") final SupportedLinkType type);

    Set<Link> findByLinkIdAndType(@Param("link_id") final Long linkId,
                                  @Param("type") final SupportedLinkType type);

}
