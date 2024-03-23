package com.anae.api.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "inscription_history")
public class InscriptionHistory {

    @Id
    @Column(name = "id_member")
    private Integer idMember;

    private String etablishment;

    private String study;

    private LocalDate date;

    //@Snico problem, un etudiant peut pas avoir plusieurs historique d'inscription?
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_member")
    private StudentData student;
}
