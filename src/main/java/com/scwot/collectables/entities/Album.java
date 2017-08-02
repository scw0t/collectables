package com.scwot.collectables.entities;

import lombok.Builder;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Value
@Builder
@Entity
public class Album {

    @Id
    @GeneratedValue
    private Long id;


}
