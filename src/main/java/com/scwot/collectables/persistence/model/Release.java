package com.scwot.collectables.persistence.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.scwot.collectables.enums.ReleaseSecondaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Release {

    @Id
    @GeneratedValue
    @Column(name = "release_id")
    private Long releaseId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sortName;

    private String barcode;

    private String catalogNumber;

    private String yearRecorded;

    private String yearReleased;

    private String country;

    @Enumerated(EnumType.STRING)
    private ReleaseSecondaryType status;

    private String quality;

    private String language;

    private String path;

    @Lob
    private byte[] image;

    @Lob
    private byte[] thumbImage;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    private LocalDateTime modifiedAt;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "release_group_id")
    private ReleaseGroup releaseGroup;

    @Builder.Default
    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL)
    private Set<Medium> mediums = Sets.newHashSet();

    @Builder.Default
    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL)
    private List<Medium> catalogueList = Lists.newArrayList();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_release",
            joinColumns = @JoinColumn(name = "release_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<Link> links = Sets.newHashSet();

    public void addLink(final Link link)
    {
        links.add(link);
        link.getReleases().add(this);
    }

    public void removeLink(final Link link)
    {
        links.remove(link);
        link.getReleases().remove(this);
    }

}
