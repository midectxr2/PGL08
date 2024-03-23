package com.anae.api.Repository;

import com.anae.api.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseDataRepository extends JpaRepository<Course, String> {
    Optional<Course> findByCourseId(String courseId);

    Optional<Course> findByCourseIdAndLinkedCursusCursusId(String courseId, String cursusId);
}
