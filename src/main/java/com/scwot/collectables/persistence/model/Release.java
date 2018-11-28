package com.scwot.collectables.persistence.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Release {

    @Id
    @GeneratedValue
    private Long releaseId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "releaseGroupId")
    private ReleaseGroup releaseGroup;

    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL)
    private List<Medium> mediumList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "labelId")
    private Label label;

    private String barcode;

    private String catalogNumber;

    private String yearRecorded;

    private String yearReleased;

    private String country;

    private String status;

    private String type;

    private byte[] image;

    private Instant dateAdded;

}