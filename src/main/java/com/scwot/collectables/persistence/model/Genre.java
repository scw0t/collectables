package com.scwot.collectables.persistence.model;

import com.google.common.collect.Sets;
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
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "genre_release_group",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "release_group_id"))
    private Set<ReleaseGroup> releaseGroups = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "genre_artist",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists = Sets.newHashSet();

    public void addArtist(final Artist artist) {
        artists.add(artist);
        artist.getGenres().add(this);
    }

    public void removeArtist(final Artist artist) {
        artists.remove(artist);
        artist.getGenres().remove(this);
    }

    public void addReleaseGroup(final ReleaseGroup releaseGroup) {
        releaseGroups.add(releaseGroup);
        releaseGroup.getGenres().add(this);
    }

    public void removeReleaseGroup(final ReleaseGroup releaseGroup) {
        releaseGroups.remove(releaseGroup);
        releaseGroup.getGenres().remove(this);
    }

}
