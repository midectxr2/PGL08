package com.anae.api.Entities;

import com.anae.api.Enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student")
@RequiredArgsConstructor
public class StudentData {

    @Id
    private Integer matricule;

    @Column(name = "inscription_status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    @OneToOne
    @MapsId
    @JoinColumn(name = "matricule")
    private UserData userData;

    //cursus doesn't have to be null if you want to register a new student
    @ManyToOne
    @JoinColumn(name = "cursus_id", nullable = false)
    private Cursus cursus;

    @OneToOne(mappedBy = "student")
    @PrimaryKeyJoinColumn
    private InscriptionHistory inscriptionHistory;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "procedure_id")
    private AcademicProcedure procedure;


//TODO make field for futureEtudiantId, extension de Nico
}
