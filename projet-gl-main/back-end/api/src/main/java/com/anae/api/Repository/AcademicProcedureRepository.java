package com.anae.api.Repository;

import com.anae.api.Entities.AcademicProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicProcedureRepository extends JpaRepository<AcademicProcedure, Integer> {
    void deleteById(Integer id);

    void removeById(Integer id);
}