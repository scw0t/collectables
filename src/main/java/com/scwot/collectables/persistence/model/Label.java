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
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue
    private Long labelId;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "label", cascade = CascadeType.ALL)
    private List<Catalogue> catalogueList;

}
