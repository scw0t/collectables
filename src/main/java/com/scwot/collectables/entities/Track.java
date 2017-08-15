package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@Builder
public class Track {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    private Medium medium;

    private String name;

    private Integer position;

    private String length;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Artist> artistList;

}
