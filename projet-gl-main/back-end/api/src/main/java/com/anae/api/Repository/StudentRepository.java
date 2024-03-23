package com.anae.api.Repository;

import com.anae.api.Entities.StudentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentData, Integer> {
    Optional<StudentData> findByMatricule(Integer matricule);

    Optional<StudentData> findByUserDataSchoolEmail(String schoolMail);

    Optional<StudentData> findByUserDataPrivateEmail(String privateEmail);
}
