package com.anae.api.Entities;

import com.anae.api.Enums.SessionStatus;
import com.anae.api.Enums.SessionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime duration;


    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @OneToOne(mappedBy = "relatedSession")
    private SessionModifyRequest modifyRequest;
}
