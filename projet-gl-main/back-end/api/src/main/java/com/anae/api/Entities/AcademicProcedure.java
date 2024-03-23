package com.anae.api.Entities;

import com.anae.api.Enums.ProcedureType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "academic_procedure")
public class AcademicProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcedureType type;


    @OneToOne(mappedBy = "procedure")
    private StudentData studentData;

}