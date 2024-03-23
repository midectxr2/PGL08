package com.anae.api.Repository;

import com.anae.api.Entities.Role;
import com.anae.api.Enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(UserRole authority);
}
