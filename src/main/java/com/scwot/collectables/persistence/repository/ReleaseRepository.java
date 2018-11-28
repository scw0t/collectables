package com.scwot.collectables.persistence.repository;

import com.scwot.collectables.persistence.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
