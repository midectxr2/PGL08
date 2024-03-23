package com.anae.api.DTOs;

import com.anae.api.Enums.RegistrationStatus;

public record StudentDTO(
        String firstname,
        String lastname,
        Integer matricule,
        String email,
        String schoolEmail,
        RegistrationStatus registrationStatus,
        String cursusTitle
) {
} //As we know he's a student, not need to include Role
//Todo note: Or maybe yes for other extension
