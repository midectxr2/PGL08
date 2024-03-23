package com.anae.api.Services;

import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.Entities.StudentData;
import com.anae.api.Enums.RegistrationStatus;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.StudentRepository;
import com.anae.api.Repository.UserDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserDataRepository userDataRepository;
    private final AuthenticationService authenticationService;
    private final CursusService cursusService;


    public StudentData registerStudent(RegistrationDTO registrationDTO, String cursusId) {

        var userData = userDataRepository.findBySchoolEmail(
                authenticationService.registerUser(registrationDTO, UserRole.STUDENT)
                        .schoolEmail()
        ).orElseThrow();

        try {
            var student = new StudentData();
            student.setStatus(RegistrationStatus.InProgress);

            student.setUserData(userData);
            student.setCursus(cursusService.loadCursus(cursusId));

            return studentRepository.save(student);
        } catch (Exception e) {
            userDataRepository.delete(userData);

            return null;
        }
    }

    public List<StudentData> getAll() {
        return studentRepository.findAll();
    }

    public StudentData loadStudent(Integer matricule) {
        return studentRepository.findByMatricule(matricule).orElseThrow();
    }

    public boolean save(StudentData student) {
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
