package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "forum")
public class Forum {

    @Id
    @Column(name = "id_forum")
    private Integer idForum;

    private String name;

    @OneToMany(
            mappedBy = "forum",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<Message> messages;
}
