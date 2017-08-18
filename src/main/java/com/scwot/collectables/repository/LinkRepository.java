package com.scwot.collectables.repository;

import com.scwot.collectables.entities.Link;
import com.scwot.collectables.enums.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    void deleteByIdAndLinkType(@Param("id") final Long id,
                               @Param("linkType") final LinkType linkType);

    List<Link> findByIdAndLinkType(@Param("id") final Long id,
                                   @Param("linkType") final LinkType linkType);

}
