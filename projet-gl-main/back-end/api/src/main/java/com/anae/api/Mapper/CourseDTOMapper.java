package com.anae.api.Mapper;

import com.anae.api.DTOs.CourseDTO;
import com.anae.api.Entities.Course;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseDTOMapper implements Function<Course, CourseDTO> {
    @Override
    public CourseDTO apply(Course course) {
        return new CourseDTO(course.getCourseId(), course.getTitle(), course.getCredits());
    }
}
