package com.scwot.collectables.persistence.repository;

import com.scwot.collectables.persistence.model.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumRepository extends JpaRepository<Medium, Long> {
}
