package com.anae.api.Controllers;

import com.anae.api.DTOs.AcademicProcedureDTO;
import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.Enums.ProcedureType;
import com.anae.api.Mapper.AcademicProcedureDTOMapper;
import com.anae.api.Services.AcademicProcedureService;
import com.anae.api.Services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/procedures")
public class AcademicProcedureController {
    private final AcademicProcedureService academicProcedureService;
    private final AcademicProcedureDTOMapper academicProcedureDTOMapper;
    private final StudentService studentService;

    public AcademicProcedureController(
            AcademicProcedureService academicProcedureService,
            AcademicProcedureDTOMapper academicProcedureDTOMapper,
            StudentService studentService) {
        this.academicProcedureService = academicProcedureService;
        this.academicProcedureDTOMapper = academicProcedureDTOMapper;
        this.studentService = studentService;
    }

    @PostMapping("/{matricule}/send")
    public ResponseEntity<String> sendProcedure(
            @PathVariable Integer matricule,
            @RequestParam ProcedureType type
    ) {
        academicProcedureService.sendProcedure(matricule, type);
        return new ResponseEntity<>("Your request has been saved", HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<AcademicProcedureDTO>> getProcedures() {
        var procedures = academicProcedureService.getAll();

        var responseDTO = procedures
                .stream()
                .map(academicProcedureDTOMapper)
                .toList();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/sendResponse")
    public ResponseEntity<String> sendResponse(
            @RequestBody DirectUserNotificationDTO notification
    ) {
        academicProcedureService.sendResponse(notification);
        return ResponseEntity.ok("Student has been notified");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProcedure(@PathVariable Integer id) {
        var student = academicProcedureService.get(id).getStudentData();
        student.setProcedure(null);

        studentService.save(student);
        return ResponseEntity.ok("deleted");
    }
}
