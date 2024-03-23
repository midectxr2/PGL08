package com.anae.api.Controllers;

import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.DTOs.StudentDTO;
import com.anae.api.Mapper.StudentDTOMapper;
import com.anae.api.Services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final StudentDTOMapper studentDTOMapper;

    public StudentController(StudentService studentService, StudentDTOMapper studentDTOMapper) {
        this.studentService = studentService;
        this.studentDTOMapper = studentDTOMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> registerStudent(
            @RequestParam String cursusId,
            @RequestBody RegistrationDTO registrationDTO
    ) {

        if (studentService.registerStudent(registrationDTO, cursusId) != null)
            return new ResponseEntity<>("Student registered", HttpStatus.CREATED);

        return ResponseEntity.badRequest().body("Something went wrong :(");
    }

    @GetMapping("/{matricule}")
        public ResponseEntity<StudentDTO> getStudent(@PathVariable("matricule") Integer matricule) {
            var student = studentService.loadStudent(matricule);

            return ResponseEntity.ok(studentDTOMapper.apply(student));
}
}
