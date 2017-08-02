package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Value
@Builder
@Entity
public class Artist {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String from;
    private String to;
    private String country;
    private List<String> links;
    private List<Person> members;
    private List<Artist> memberOf;
    private List<Artist> related;
    private List<String> aka;
    private List<Genre> genres;


}
