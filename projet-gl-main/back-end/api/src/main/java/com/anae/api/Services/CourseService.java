package com.anae.api.Services;

import com.anae.api.Entities.Course;
import com.anae.api.Repository.CourseDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseService {
    private final CourseDataRepository courseDataRepository;

    public List<Course> getCourses() {
        return courseDataRepository.findAll();
    }

    public Course getCourse(String courseId) {
        return courseDataRepository.findByCourseId(courseId).orElseThrow();
    }

    public void addCourse(Course course) {
        courseDataRepository.save(course);
    }

    public boolean isLinkedCursus(String courseId, String cursusId) {
        return courseDataRepository.findByCourseIdAndLinkedCursusCursusId(courseId, cursusId).isPresent();
    }
}
