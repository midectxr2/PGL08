package com.anae.api.DTOs;

import com.anae.api.Enums.ProcedureType;

/**
 * DTO for {@link com.anae.api.Entities.AcademicProcedure}
 */
public record AcademicProcedureDTO(
        ProcedureType type,
        StudentDTO studentData
) {
}