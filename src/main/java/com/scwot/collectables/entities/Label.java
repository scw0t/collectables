package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
public class Label {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

}
