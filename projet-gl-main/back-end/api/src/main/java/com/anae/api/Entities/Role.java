package com.anae.api.Entities;

import com.anae.api.Enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Integer roleId;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole authority;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "authorities"
    )
    private Set<UserData> users;

    @OneToOne(mappedBy = "authority")
    private FutureStudentData futureStudentData;

    public Role() {
        super();
        users = new HashSet<>();
    }

    public Role(UserRole authority) {
        this.authority = authority;
    }

    public Role(Integer roleId, UserRole authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.name();
    }
}
