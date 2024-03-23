package com.anae.api.Repository;

import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    List<UserData> findByAuthoritiesAuthority(Optional<UserRole> role);

    Optional<UserData> findByPrivateEmail(String privateEmail);

    Optional<UserData> findBySchoolEmail(String schoolEmal);

    Optional<UserData> findByMatricule(Integer matricule);

    Optional<UserData> findByMatriculeAndAuthoritiesAuthority(Integer matricule, UserRole userRole);

    Integer deleteByMatricule(Integer matricule);
}
