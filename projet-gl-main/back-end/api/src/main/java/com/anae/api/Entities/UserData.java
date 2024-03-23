package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "umons_members", uniqueConstraints = {@UniqueConstraint(name = "UniqueFirstAndLastName",
        columnNames = {"firstname", "lastname"})})
public class UserData implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-mat-sequence")
    @SequenceGenerator(name = "custom-mat-sequence", initialValue = 230000, allocationSize = 1)
    private Integer matricule;

    private String firstname;

    private String lastname;

    private String password;

    @Column(name = "private_email", unique = true)
    private String privateEmail;

    @Column(name = "school_email")
    private String schoolEmail;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "matricule")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"matricule", "role_id"})
    )
    private Set<Role> authorities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id", referencedColumnName = "id")
    private Address adress;

    @OneToOne(mappedBy = "userData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private StudentData student;

    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
    private Set<Course> taughtCourses;

    @OneToOne(mappedBy = "userData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Researcher researcher;

    @OneToOne(mappedBy = "userData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Researcher coAuthor;

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages;

    @ManyToMany(mappedBy = "receveurs")
    private Set<Message> receivedMessages;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "members"
    )
    private Set<Group> groups;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "receveurs"
    )
    private Set<Meeting> meetings; //Call it as you want #Antoine

    @OneToMany(mappedBy = "userData", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DirectUserNotification> directUserNotifications;

    public UserData() {
        super();
    }

    public UserData(Integer matricule, String firstname, String lastname, String password, String privateEmail, Set<Role> authorities, Address address) {
        super();
        this.matricule = matricule;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.privateEmail = privateEmail;
        this.authorities = authorities;
        this.adress = address;
    }

    @PrePersist
    @PreUpdate
    private void generateSchoolEmail() {
        if (schoolEmail == null || schoolEmail.isEmpty()) {
            this.schoolEmail = matricule + "@nasa.ac.be";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.privateEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
