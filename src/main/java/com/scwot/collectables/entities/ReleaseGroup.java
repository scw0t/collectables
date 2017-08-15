package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@Builder
public class ReleaseGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "releaseGroup")
    private List<Release> releaseList;

    private String type;

    @OneToMany
    private List<Genre> genreList;

}
