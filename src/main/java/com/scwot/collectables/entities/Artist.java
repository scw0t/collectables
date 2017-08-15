package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
@Entity
public class Artist {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String mbid;

    private Boolean isGroup;

    @Column(nullable = false)
    private String name;

    private String from;

    private String to;

    private String country;

    @OneToMany
    private List<Link> links;

    private List<Artist> members;

    private List<Artist> memberOf;

    private List<Artist> related;

    private List<String> aka;

    private List<Genre> genres;

}
