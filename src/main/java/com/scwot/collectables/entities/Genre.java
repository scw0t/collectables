package com.scwot.collectables.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "genre_releaseGroup",
            joinColumns = @JoinColumn(name = "genreId"),
            inverseJoinColumns = @JoinColumn(name = "releaseGroupId"))
    private List<ReleaseGroup> releaseGroupList;

}
