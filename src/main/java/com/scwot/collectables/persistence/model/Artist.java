package com.scwot.collectables.persistence.model;

import com.google.common.collect.Sets;
import com.scwot.collectables.enums.ArtistType;
import com.scwot.collectables.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "artist_id")
    private Long artistId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @Column(name = "sort_name", nullable = false)
    private String sortName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "artist_type")
    private ArtistType type;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String area;

    private String disambiguation;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] thumbImage;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "artist_release_group",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "release_group_id"))
    private Set<ReleaseGroup> releaseGroups = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "artist_members",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Artist> members = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "artist_members",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> memberOf = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "genre_artist",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_artist",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<Link> links = Sets.newHashSet();


    public void addReleaseGroup(final ReleaseGroup releaseGroup) {
        releaseGroups.add(releaseGroup);
        releaseGroup.getArtists().add(this);
    }

    public void removeReleaseGroup(final ReleaseGroup releaseGroup) {
        releaseGroups.remove(releaseGroup);
        releaseGroup.getArtists().remove(this);
    }

    public void removeAllReleaseGroups() {
        for (ReleaseGroup releaseGroup : releaseGroups) {
            removeReleaseGroup(releaseGroup);
        }
    }

    public void addMember(final Artist artist) {
        members.add(artist);
        artist.getMemberOf().add(this);
    }

    public void removeMember(final Artist artist) {
        members.remove(artist);
        artist.getMemberOf().remove(this);
    }

    public void removeAllMembers() {
        for (Artist member : members) {
            removeMember(member);
        }
    }

    public void addGenre(final Genre genre)
    {
        genres.add(genre);
        genre.getArtists().add(this);
    }

    public void removeGenre(final Genre genre)
    {
        genres.remove(genre);
        genre.getArtists().remove(this);
    }

    public void addLink(final Link link)
    {
        links.add(link);
        link.getArtists().add(this);
    }

    public void removeLink(final Link link)
    {
        links.remove(link);
        link.getArtists().remove(this);
    }



}
