package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
public class Genre {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
