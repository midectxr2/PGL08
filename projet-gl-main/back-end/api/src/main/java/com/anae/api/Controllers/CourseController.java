package com.anae.api.Controllers;

import com.anae.api.DTOs.CourseDTO;
import com.anae.api.Entities.Course;
import com.anae.api.Enums.UserRole;
import com.anae.api.Mapper.CourseDTOMapper;
import com.anae.api.Repository.CourseDataRepository;
import com.anae.api.Repository.CursusDataRepository;
import com.anae.api.Repository.UserDataRepository;
import com.anae.api.Users.Secretary;
import com.anae.api.Users.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.anae.api.Services.TokenService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseDataRepository courseDataRepository;
    private final UserDataRepository userDataRepository;
    private final CursusDataRepository cursusDataRepository;
    private final CourseDTOMapper courseDTOMapper;
    private final TokenService tokenService;

    public CourseController(CourseDataRepository courseDataRepository, UserDataRepository userDataRepository,
            CursusDataRepository cursusDataRepository, CourseDTOMapper courseDTOMapper, TokenService tokenService) {
        this.courseDataRepository = courseDataRepository;
        this.userDataRepository = userDataRepository;
        this.cursusDataRepository = cursusDataRepository;
        this.courseDTOMapper = courseDTOMapper;
        this.tokenService = tokenService;
    }

    @GetMapping
    public List<CourseDTO> getCourses() {
        return courseDataRepository.findAll().stream().map(courseDTOMapper).toList();
    }

    @GetMapping("/{matricule}")
    public List<CourseDTO> getCoursesSpec(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("matricule") int matricule) {
        if (tokenService.tokenContainsRole(authorizationHeader, UserRole.SECRETARY)) {
            return courseDataRepository.findAll().stream().map(courseDTOMapper).toList();
        } else {
            Teacher teacher = new Teacher(matricule, userDataRepository, courseDataRepository);
            return teacher.getUserData().getTaughtCourses().stream().map(courseDTOMapper).toList();
        }

    }

    @PatchMapping(path = "/modify")
    public ResponseEntity<String> modifyCourse(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Course modifiedCourse,
            @RequestParam(value = "matricule") Integer matricule) {
        if (tokenService.tokenContainsRole(authorizationHeader, UserRole.TEACHER)) {
            Teacher teacher = new Teacher(matricule, userDataRepository, courseDataRepository);
            teacher.changeCourseTitle(modifiedCourse.getCourseId(), modifiedCourse.getTitle());
            return ResponseEntity.ok("Title modified");
        } else {
            Secretary secretary = new Secretary(matricule, userDataRepository, courseDataRepository);
            secretary.changeCourseCredit(modifiedCourse.getCourseId(), modifiedCourse.getCredits());
            return ResponseEntity.ok("Course credit modified");
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewCourse(
            @RequestBody Course newCourse,
            @RequestParam(value = "matricule") Integer matricule) {
        Secretary secretary = new Secretary(matricule, userDataRepository, courseDataRepository);
        if (secretary.createCourse(newCourse)){
            return new ResponseEntity<>("Course created", HttpStatus.CREATED);
        }
        return ResponseEntity.ok("Course already created");
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteCourse(
            @RequestParam(value = "matricule") Integer matricule,
            @RequestParam(value = "courseId") String courseId) {
        Secretary secretary = new Secretary(matricule, userDataRepository, courseDataRepository);
        secretary.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted");
    }

}
