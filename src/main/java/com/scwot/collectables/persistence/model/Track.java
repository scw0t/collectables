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
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @Id
    @GeneratedValue
    @Column(name = "track_id")
    private Long trackId;

    @Column(nullable = false)
    private String name;

    private Integer position;

    private Long length;

    private String path;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "medium_id")
    private Medium medium;
}
