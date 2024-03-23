package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CoAuthor {
    @Id
    @Column(name = "id_member")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_publication", referencedColumnName = "id_publication")
    private Publication publication;

    @OneToOne
    @MapsId
    @JoinColumn(name = "matricule")
    private UserData userData;
}
