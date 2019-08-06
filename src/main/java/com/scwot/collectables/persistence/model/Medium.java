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
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Medium {

    @Id
    @GeneratedValue
    private Long mediumId;

    @Column(nullable = false)
    private String name;

    @Column(name = "sort_name", nullable = false)
    private String sortName;

    private String format;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "release_id")
    private Release release;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "track_id")
    private List<Track> trackList;

}
