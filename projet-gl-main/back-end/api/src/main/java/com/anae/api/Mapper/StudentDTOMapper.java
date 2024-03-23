package com.anae.api.Mapper;

import com.anae.api.DTOs.StudentDTO;
import com.anae.api.Entities.StudentData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentDTOMapper implements Function<StudentData, StudentDTO> {
    @Override
    public StudentDTO apply(StudentData student) {
        var sd = student.getUserData();
        return new StudentDTO(
                sd.getFirstname(),
                sd.getLastname(),
                sd.getMatricule(),
                sd.getPrivateEmail(),
                sd.getSchoolEmail(),
                student.getStatus(),
                student.getCursus().getTitle()
        );
    }
}
