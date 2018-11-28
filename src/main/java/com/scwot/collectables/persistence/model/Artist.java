package com.scwot.collectables.persistence.model;

import com.scwot.collectables.enums.Gender;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue
    private Long artistId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sortName;

    private Gender gender;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_releaseGroup",
            joinColumns = @JoinColumn(name = "artistId"),
            inverseJoinColumns = @JoinColumn(name = "releaseGroupId"))
    private List<ReleaseGroup> releaseGroupList;

    private Boolean isGroup;

    private String beginDate;

    private String endDate;

    private String area;

    @ManyToMany
    @JoinTable(name = "artist_members",
            joinColumns = @JoinColumn(name = "artistId"),
            inverseJoinColumns = @JoinColumn(name = "memberId"))
    private List<Artist> members;

    @ManyToMany
    @JoinTable(name = "artist_members",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "artistId"))
    private List<Artist> memberOf;

    @Lob
    private byte[] image;

    /*private List<Genre> genres;*/

}
