package com.scwot.collectables.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String mbid;

    @Column(nullable = false)
    private String name;

}
