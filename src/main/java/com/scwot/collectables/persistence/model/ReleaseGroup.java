package com.scwot.collectables.persistence.model;

import com.google.common.collect.Sets;
import com.scwot.collectables.enums.ReleasePrimaryType;
import com.scwot.collectables.enums.ReleaseSecondaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "release_group")
public class ReleaseGroup {

    @Id
    @GeneratedValue
    @Column
    private Long releaseGroupId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sortName;

    private String albumArtistTitle;

    private Boolean va;

    @Enumerated(EnumType.STRING)
    private ReleasePrimaryType primaryType;

    @ElementCollection(targetClass = ReleaseSecondaryType.class)
    @Enumerated(EnumType.STRING)
    private Set<ReleaseSecondaryType> secondaryTypes;

    @Lob
    private byte[] image;

    @Lob
    private byte[] thumbImage;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    private LocalDateTime modifiedAt;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "releaseGroup"
    )
    private Set<Release> releases = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "artist_release_group",
            joinColumns = @JoinColumn(name = "release_group_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "genre_release_group",
            joinColumns = @JoinColumn(name = "release_group_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_release_group",
            joinColumns = @JoinColumn(name = "release_group_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<Link> links = Sets.newHashSet();

    public void addArtist(final Artist artist) {
        artists.add(artist);
        artist.getReleaseGroups().add(this);
    }

    public void removeArtist(final Artist artist) {
        artists.remove(artist);
        artist.getReleaseGroups().remove(this);
    }

    public void addGenre(final Genre genre) {
        genres.add(genre);
        genre.getReleaseGroups().remove(this);
    }

    public void removeGenre(final Genre genre) {
        genres.remove(genre);
        genre.getReleaseGroups().remove(this);
    }

    public void addLink(final Link link)
    {
        links.add(link);
        link.getReleaseGroups().add(this);
    }

    public void removeLink(final Link link)
    {
        links.remove(link);
        link.getReleaseGroups().remove(this);
    }

}
