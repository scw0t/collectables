package com.scwot.collectables.persistence.model;

import com.google.common.collect.Sets;
import com.scwot.collectables.enums.SupportedLinkType;
import com.scwot.collectables.persistence.core.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Link {

    @Id
    @GeneratedValue
    private Long linkId;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    private SupportedLinkType supportedLinkType;

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_artist",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_release_group",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "release_group_id"))
    private Set<ReleaseGroup> releaseGroups = Sets.newHashSet();

    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "link_release",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "release_id"))
    private Set<Release> releases = Sets.newHashSet();

}
