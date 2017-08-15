package com.scwot.collectables.entities;

import com.scwot.collectables.enums.LinkResource;
import com.scwot.collectables.enums.LinkType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Link {

    @Id
    @GeneratedValue
    private Long id;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Enumerated(EnumType.STRING)
    private LinkResource linkResource;

}
