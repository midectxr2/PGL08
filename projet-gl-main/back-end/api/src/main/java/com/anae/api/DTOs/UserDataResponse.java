package com.anae.api.DTOs;

import java.util.List;

public record UserDataResponse(
        String firstname,
        String lastname,
        Integer matricule,
        String privateEmail,
        String schoolEmail,
        List<String> roles,
        AddressDTO address
) {
}
