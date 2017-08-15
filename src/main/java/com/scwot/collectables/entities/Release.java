package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@Builder
public class Release {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private ReleaseGroup releaseGroup;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Medium> mediumList;

    private String barcode;

    private Label label;

    private String catalogNumber;

    private String yearRecorded;
    private String yearReleased;

}
