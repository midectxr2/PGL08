package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Meeting {
    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Integer meetingId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 20)
    private String location;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "asker", nullable = false)
    private UserData asker;

    @ManyToMany
    @JoinTable(
            name = "meeting_table",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "receveur_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"meeting_id", "receveur_id"})
    )
    private Set<UserData> receveurs; //Why a many to many between meetings and receveurs?
}
