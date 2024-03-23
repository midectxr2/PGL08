package com.anae.api.DTOs;

public record LoginResponse(
        Integer matricule,
        String jwt
) {
}
