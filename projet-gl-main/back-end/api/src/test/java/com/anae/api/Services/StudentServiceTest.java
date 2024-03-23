package com.anae.api.Services;

import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.DTOs.RegistrationResponse;
import com.anae.api.Entities.Cursus;
import com.anae.api.Entities.StudentData;
import com.anae.api.Entities.UserData;
import com.anae.api.Repository.StudentRepository;
import com.anae.api.Repository.UserDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CursusService cursusService;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository, userDataRepository, authenticationService, cursusService);
    }

    @AfterEach
    void tearDown() {
        userDataRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void registerStudentSuccess() {
        var registrationData = new RegistrationDTO(
                "myFirstname",
                "",
                "test@example.be",
                "1234",
                "Example street",
                "",
                "",
                "",
                0,
                7000,
                ""
        );

        var userData = new UserData();
        userData.setMatricule(230000);

        var cursus = new Cursus();

        when(authenticationService.registerUser(any(), any())).thenReturn(new RegistrationResponse("230000@nasa.ac.be"));
        when(userDataRepository.findBySchoolEmail(anyString())).thenReturn(Optional.of(userData));
        when(cursusService.loadCursus(anyString())).thenReturn(cursus);

        studentService.registerStudent(registrationData, "testId");

        verify(studentRepository).save(any());
        verify(userDataRepository, never()).delete(any());
    }

    @Test
    void registerStudentFail() {
        var registrationData = new RegistrationDTO(
                "myFirstname",
                "",
                "test@example.be",
                "1234",
                "Example street",
                "",
                "",
                "",
                0,
                7000,
                ""
        );

        var userData = new UserData();
        userData.setMatricule(230000);

        var cursus = new Cursus();

        when(authenticationService.registerUser(any(), any())).thenReturn(new RegistrationResponse("230000@nasa.ac.be"));
        when(userDataRepository.findBySchoolEmail(anyString())).thenReturn(Optional.of(userData));
        when(cursusService.loadCursus(anyString())).thenReturn(cursus);
        when(studentRepository.save(any())).thenThrow(IllegalArgumentException.class);

        studentService.registerStudent(registrationData, "testId");

        verify(studentRepository).save(any());
        verify(userDataRepository).delete(any());
    }

    @Test
    void shouldGetAll() {
        studentService.getAll();

        verify(studentRepository).findAll();
    }

    @Test
    void shouldLoadStudentOnValidMatricule() {
        var student = new StudentData();
        student.setMatricule(230000);

        when(studentRepository.findByMatricule(230000)).thenReturn(Optional.of(student));

        studentService.loadStudent(230000);

        verify(studentRepository).findByMatricule(anyInt());
    }

    @Test
    void shouldFailToLoadStudentOnValidMatricule() {
        assertThatThrownBy(
                () -> studentService.loadStudent(230000)
        ).isInstanceOf(NoSuchElementException.class);

        verify(studentRepository).findByMatricule(anyInt());
    }

    @Test
    void shouldReturnTrueOnSuccessSaveOfStudent() {
        var student = new StudentData();

        boolean response = studentService.save(student);

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseOnFailToSaveOfStudent() {
        var student = new StudentData();

        when(studentRepository.save(any())).thenThrow(IllegalArgumentException.class);

        boolean response = studentService.save(student);

        assertThat(response).isFalse();
    }
}