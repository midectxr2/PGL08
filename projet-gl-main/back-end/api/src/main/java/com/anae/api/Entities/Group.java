package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id_group")
    private Integer idGroup;

    private Integer name;

    @OneToMany(
            mappedBy = "group",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<Message> messages;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "groups_members",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "member_id"})
    )
    private Set<UserData> members;
}
