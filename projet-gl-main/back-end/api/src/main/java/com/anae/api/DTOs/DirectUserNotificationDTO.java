package com.anae.api.DTOs;

/**
 * DTO for {@link com.anae.api.Entities.DirectUserNotification}
 */
public record DirectUserNotificationDTO(
        Integer matricule,
        String message
) {
}