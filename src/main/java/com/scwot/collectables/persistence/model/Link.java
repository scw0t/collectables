package com.scwot.collectables.persistence.model;

import com.scwot.collectables.enums.LinkResource;
import com.scwot.collectables.enums.LinkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkResource linkResource;

}
