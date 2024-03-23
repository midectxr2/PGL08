package com.anae.api.Repository;

import com.anae.api.Entities.Cursus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursusDataRepository extends JpaRepository<Cursus, String> {
    Optional<Cursus> findByCursusId(String id);

    Optional<List<Cursus>> findByCoursesCourseId(String courseId);

    boolean existsByCursusId(String cursusId);
}
