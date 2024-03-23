package com.anae.api.Repository;

import com.anae.api.Entities.DirectUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface DirectUserNotificationRepository extends JpaRepository<DirectUserNotification, Integer> {
    void deleteById(@NonNull Integer id);
    void deleteAllByUserDataMatricule(@NonNull Integer id);

    Optional<List<DirectUserNotification>> findAllByUserDataMatricule(Integer matricule);
}