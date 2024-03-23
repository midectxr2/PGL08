package com.anae.api.Mapper;

import com.anae.api.DTOs.AcademicProcedureDTO;
import com.anae.api.Entities.AcademicProcedure;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AcademicProcedureDTOMapper implements Function<AcademicProcedure, AcademicProcedureDTO> {
    private final StudentDTOMapper studentDTOMapper;

    public AcademicProcedureDTOMapper(StudentDTOMapper studentDTOMapper) {
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public AcademicProcedureDTO apply(AcademicProcedure academicProcedure) {
        var studentDTO = studentDTOMapper.apply(academicProcedure.getStudentData());

        return new AcademicProcedureDTO(
                academicProcedure.getType(),
                studentDTO
        );
    }
}
