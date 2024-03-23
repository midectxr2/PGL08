package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "course_id")
    private String courseId;

    //Should be nullable = false
    //@Column(nullable = false)
    private String title;

    private Integer credits;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "matricule"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "matricule"})
    )
    private Set<UserData> teachers;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "courses"
    )
    private Set<Cursus> linkedCursus;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Session> sessions;

    @PreRemove
    private void removeCursusAssociation(){
        for (Cursus cursus:linkedCursus){
            cursus.getCourses().remove(this);
        }
    }
}
