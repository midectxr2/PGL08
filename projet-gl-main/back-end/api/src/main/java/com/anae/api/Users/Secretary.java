package com.anae.api.Users;

import com.anae.api.Entities.Course;
import com.anae.api.Repository.CourseDataRepository;
import com.anae.api.Repository.UserDataRepository;

public class Secretary extends User {

    private final UserDataRepository userDataRepository;
    private final CourseDataRepository courseDataRepository;

    public Secretary(int matricule, UserDataRepository userDataRepository, CourseDataRepository coursesDataRepository) {
        super(matricule, userDataRepository);
        this.userDataRepository = userDataRepository;
        this.courseDataRepository = coursesDataRepository;
    }

    public boolean deleteCourse(String courseId) {
        Course course = courseDataRepository.findByCourseId(courseId).orElseThrow();
        courseDataRepository.delete(course);
        return true;
    }

    public boolean createCourse(Course newCourse) {
        if (courseDataRepository.findByCourseId(newCourse.getCourseId()).orElse(null) == null){
            courseDataRepository.save(newCourse);
            return true;
        }
        return false;
    }

    public boolean changeCourseCredit(String courseId, int newCredit) {
        Course course = courseDataRepository.findByCourseId(courseId).orElseThrow();
        course.setCredits(newCredit);
        courseDataRepository.save(course);
        return true;
    }

}
