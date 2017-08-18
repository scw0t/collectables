package com.scwot.collectables.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Medium {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String format;

    @OneToMany
    private List<Track> trackList;

}
