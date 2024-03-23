package com.anae.api.Users;

import com.anae.api.Entities.Course;
import com.anae.api.Repository.CourseDataRepository;
import com.anae.api.Repository.UserDataRepository;


public class Teacher extends User {

    private final CourseDataRepository courseDataRepository;
    private final UserDataRepository userDataRepository;

    public Teacher(int matricule, UserDataRepository userDataRepository, CourseDataRepository courseDataRepository) {
        super(matricule, userDataRepository);
        this.userDataRepository = userDataRepository;
        this.courseDataRepository = courseDataRepository;
    }


    public boolean changeCourseTitle(String courseId, String newTitle) {
        //TODO: Make necessary things
        Course course = courseDataRepository.findByCourseId(courseId).orElseThrow();
        course.setTitle(newTitle);
        courseDataRepository.save(course);
        return true;
    }
}
