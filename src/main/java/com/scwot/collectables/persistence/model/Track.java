package com.scwot.collectables.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @Id
    @GeneratedValue
    private Long trackId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Medium medium;

    private String name;

    private Integer position;

    private Long length;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Artist> artistList;

}
