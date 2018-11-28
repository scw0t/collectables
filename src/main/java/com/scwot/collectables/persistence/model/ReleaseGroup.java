package com.scwot.collectables.persistence.model;

import com.scwot.collectables.enums.ReleaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseGroup {

    @Id
    @GeneratedValue
    private Long releaseGroupId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "releaseGroup")
    private List<Release> releaseList;

    private ReleaseType type;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_releaseGroup",
            joinColumns = @JoinColumn(name = "releaseGroupId"),
            inverseJoinColumns = @JoinColumn(name = "artistId"))
    private Set<Artist> artistSet;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "genre_releaseGroup",
            joinColumns = @JoinColumn(name = "releaseGroupId"),
            inverseJoinColumns = @JoinColumn(name = "genreId"))
    private List<Genre> genreList;

    private Boolean va;

}
