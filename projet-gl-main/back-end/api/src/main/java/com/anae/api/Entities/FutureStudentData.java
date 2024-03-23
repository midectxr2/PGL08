package com.anae.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//TODO Créer un Users FutureEtudiant detiendra un FutureStudentData et un InscriptionFormulary (tout deux des Entity)
@Getter
@Setter
@Entity
@Table(name = "future_student")
public class FutureStudentData implements UserDetails {

    @Id
    private Integer id;

    private String mail;

    private String password;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.mail;
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
