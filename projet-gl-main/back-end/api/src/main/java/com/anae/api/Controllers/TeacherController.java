package com.anae.api.Controllers;

import com.anae.api.Services.CourseService;
import com.anae.api.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final UserService userService;
    private final CourseService courseService;

    public TeacherController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @PostMapping("/{matricule}/affect")
    public ResponseEntity<String> affectTo(
            @PathVariable Integer matricule,
            @RequestParam String courseId
    ) {
        var course = courseService.getCourse(courseId);

        if (userService.affectTeacher(matricule, course))
            return ResponseEntity.ok(
                    "Teacher " + matricule
                            + " Affected to course: " + courseId
            );

        return new ResponseEntity<>("Teacher not found", HttpStatus.NOT_FOUND);
    }
}
