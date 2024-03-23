package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @Column(name = "message_id")
    private Integer messageId;

    @Column(nullable = false)
    private String content;

    private String object;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserData sender;

    @ManyToMany
    @JoinTable(
            name = "conversation",
            joinColumns = {@JoinColumn(name = "receveur_id")},
            inverseJoinColumns = {@JoinColumn(name = "message_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"receveur_id", "message_id"})}
    )
    private Set<UserData> receveurs;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @PrePersist
    @PreUpdate
    private void validateFields() {
        if (group == null && forum == null) {
            throw new IllegalArgumentException("Get your own error message #Antoine");
        }
        if (group != null && forum != null) {
            throw new IllegalArgumentException("Get your own error message #Antoine");

        }
    }
}