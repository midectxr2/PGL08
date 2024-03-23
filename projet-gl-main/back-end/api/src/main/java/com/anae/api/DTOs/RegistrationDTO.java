package com.anae.api.DTOs;

public record RegistrationDTO(
        String firstname,
        String lastname,
        String email,
        String password,
        String address,
        String address2,
        String city,
        String country,
        Integer phonenumber,
        Integer postal_code,
        String region
) {
}
