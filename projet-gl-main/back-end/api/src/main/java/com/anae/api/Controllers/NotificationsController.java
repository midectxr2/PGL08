package com.anae.api.Controllers;

import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.Repository.DirectUserNotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifs")
public class NotificationsController {

    private final DirectUserNotificationRepository directUserNotificationRepository;

    public NotificationsController(DirectUserNotificationRepository directUserNotificationRepository) {
        this.directUserNotificationRepository = directUserNotificationRepository;
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<List<DirectUserNotificationDTO>> getDirectNotifications(
            @PathVariable Integer matricule
    ) {
        var notifs = directUserNotificationRepository.findAllByUserDataMatricule(matricule)
                .orElseThrow()
                .stream()
                .map((directUserNotification -> new DirectUserNotificationDTO(matricule, directUserNotification.getMessage())))
                .toList();

        return ResponseEntity.ok(notifs);
    }

    @GetMapping("/{matricule}/clear")
    public ResponseEntity<String> clearDirectNotifications(
            @PathVariable Integer matricule) {
        if (matricule != null) {

            directUserNotificationRepository.deleteAllByUserDataMatricule(matricule);
            return ResponseEntity.ok("cleared");
        }

        return ResponseEntity.badRequest().build();
    }
}
