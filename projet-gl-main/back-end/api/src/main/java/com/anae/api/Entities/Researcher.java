package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Researcher {

    @Id
    @Column(name = "id_researcher")
    private Integer id;

    @Column(name = "publication_amount")
    private Integer publicationAmount;

    @OneToMany(mappedBy = "researcher")
    private Set<Publication> publications;

    @OneToOne
    @MapsId
    @JoinColumn(name = "matricule")
    private UserData userData;
}
