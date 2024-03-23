package com.anae.api.DTOs;

/**
 * DTO for {@link com.anae.api.Entities.Address}
 */
public record AddressDTO(
        String city,
        String adress,
        String adress2,
        String region,
        Integer postalCode,
        Integer phonenumber,
        String country) {
}