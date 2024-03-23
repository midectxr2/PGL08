package com.anae.api.Services;

import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.Entities.AcademicProcedure;
import com.anae.api.Entities.DirectUserNotification;
import com.anae.api.Entities.StudentData;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.ProcedureType;
import com.anae.api.Repository.AcademicProcedureRepository;
import com.anae.api.Repository.DirectUserNotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicProcedureServiceTest {

    @Mock private StudentService studentService;

    @Mock private UserService userService;

    @Mock private AcademicProcedureRepository academicProcedureRepository;

    @Mock private DirectUserNotificationRepository directUserNotificationRepository;

    private AcademicProcedureService academicProcedureService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        academicProcedureService = new AcademicProcedureService(academicProcedureRepository, studentService, userService, directUserNotificationRepository);
    }

    @AfterEach
    void tearDown(){
        academicProcedureRepository.deleteAll();
        directUserNotificationRepository.deleteAll();
    }

    @Test
    void shouldExecuteFindAll() {
        academicProcedureService.getAll();

        verify(academicProcedureRepository).findAll();
    }

    @Test
    void shouldExecuteFindById(){
        //given
        var academicProcedure = new AcademicProcedure();
        academicProcedure.setType(ProcedureType.Registration);

        when(academicProcedureRepository.findById(any())).thenReturn(Optional.of(academicProcedure));

        //when
        var expected = academicProcedureService.get(2);

        //Then
        verify(academicProcedureRepository).findById(2);
        assertThat(expected).isEqualTo(academicProcedure);
    }

    @Test
    void shouldSendProcedureWithNonModifiedData() {
        //Given
        var student = new StudentData();
        student.setMatricule(230010);

        when(studentService.loadStudent(230010)).thenReturn(student);

        //When
        academicProcedureService.sendProcedure(230010, ProcedureType.Registration);

        //then
        ArgumentCaptor<StudentData> studentArgumentCaptor = ArgumentCaptor.forClass(StudentData.class);

        verify(studentService).save(studentArgumentCaptor.capture());

        StudentData capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
        assertThat(capturedStudent.getProcedure().getType()).isEqualTo(ProcedureType.Registration);
    }

    @Test
    void shouldSuccessToSendResponse() {
        //given
        AcademicProcedure academicProcedure = new AcademicProcedure();
        academicProcedure.setType(ProcedureType.Unregister);

        var user = new UserData();
        user.setMatricule(230010);
        user.setFirstname("Andy");

        var student = new StudentData();
        student.setProcedure(academicProcedure);

        user.setStudent(student);
        student.setUserData(user);

        when(userService.load(any())).thenReturn(user);

        //when
        academicProcedureService.sendResponse(new DirectUserNotificationDTO(230010, "Test"));

        //then
        ArgumentCaptor<StudentData> studentArgumentCaptor = ArgumentCaptor.forClass(StudentData.class);
        ArgumentCaptor<DirectUserNotification> notificationArgumentCaptor = ArgumentCaptor.forClass(DirectUserNotification.class);

        verify(directUserNotificationRepository).save(notificationArgumentCaptor.capture());
        verify(studentService).save(studentArgumentCaptor.capture());

        var capturedStudent = studentArgumentCaptor.getValue();
        var capturedNotif = notificationArgumentCaptor.getValue();

        assertThat(capturedStudent.getUserData()).isEqualTo(user);
        assertThat(capturedStudent.getProcedure()).isNull();
        assertThat(capturedNotif.getUserData()).isEqualTo(user);
    }

    @Test
    void shouldFailToSendResponse() {
        //given
        AcademicProcedure academicProcedure = new AcademicProcedure();
        academicProcedure.setType(ProcedureType.Unregister);

        var user = new UserData();
        user.setMatricule(230010);
        user.setFirstname("Andy");

        var student = new StudentData();
        student.setProcedure(academicProcedure);

        user.setStudent(student);
        student.setUserData(user);

        when(userService.load(any())).thenReturn(user);
        when(directUserNotificationRepository.save(any())).thenThrow(IllegalArgumentException.class);

        //when
        academicProcedureService.sendResponse(new DirectUserNotificationDTO(230010, "Test"));

        //then

        verify(studentService, never()).save(any());
    }
}