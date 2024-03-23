package com.anae.api.Services;

import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.Entities.AcademicProcedure;
import com.anae.api.Entities.DirectUserNotification;
import com.anae.api.Enums.ProcedureType;
import com.anae.api.Repository.AcademicProcedureRepository;
import com.anae.api.Repository.DirectUserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicProcedureService {

    private final StudentService studentService;
    private final UserService userService;
    private final AcademicProcedureRepository academicProcedureRepository;
    private final DirectUserNotificationRepository directUserNotificationRepository;

    public AcademicProcedureService(AcademicProcedureRepository academicProcedureRepository, StudentService studentService, UserService userService,
                                    DirectUserNotificationRepository directUserNotificationRepository) {
        this.academicProcedureRepository = academicProcedureRepository;
        this.studentService = studentService;
        this.userService = userService;
        this.directUserNotificationRepository = directUserNotificationRepository;
    }

    public List<AcademicProcedure> getAll() {
        return academicProcedureRepository.findAll();
    }

    public AcademicProcedure get(Integer id) {
        return academicProcedureRepository.findById(id).orElseThrow();
    }

    public void sendProcedure(Integer matricule, ProcedureType type) {
            var student = studentService.loadStudent(matricule);

            var procedure = new AcademicProcedure();
            procedure.setType(type);
            procedure.setStudentData(student);

            student.setProcedure(procedure);
            studentService.save(student);
    }

    public boolean sendResponse(DirectUserNotificationDTO notification) {
        try {
            var user = userService.load(notification.matricule());
            var newNotification = new DirectUserNotification();

            newNotification.setMessage(notification.message());
            newNotification.setUserData(user);

            directUserNotificationRepository.save(newNotification);
            System.out.println("Notification saved");

            var student = user.getStudent();
            student.setProcedure(null);

            return studentService.save(student);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
