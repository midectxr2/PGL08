package com.anae.api.Entities;

import com.anae.api.Updatable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "cursus")
public class Cursus implements Updatable {

    @Id
    @Column(name = "id")
    private String cursusId;


    private String title;

    //TODO: check if list is empty then fill it
    @ManyToMany
    @JoinTable(
            name = "cursus_courses",
            joinColumns = {@JoinColumn(name = "cursus_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courses;

    @OneToMany(mappedBy = "cursus", fetch = FetchType.LAZY)
    private Set<StudentData> students;

    public Cursus() {
        super();
        courses = new HashSet<>();
    }

    public Cursus(String cursusId, String title) {
        this.cursusId = cursusId;
        this.title = title;
    }

    @Override
    public void updateData() {
        //
    }
}
