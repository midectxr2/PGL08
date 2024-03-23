package com.anae.api.Controllers;

import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.DTOs.StudentDTO;
import com.anae.api.Entities.StudentData;
import com.anae.api.Enums.RegistrationStatus;
import com.anae.api.Mapper.StudentDTOMapper;
import com.anae.api.Services.StudentService;
import com.anae.api.Services.TokenService;
import com.anae.api.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.anae.api.Controllers.CursusControllerTest.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentDTOMapper studentDTOMapper;

    @Autowired
    @InjectMocks
    private StudentController studentController;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    private String regToken, secrToken;

    @BeforeEach
    public void setUp() {
        // Mock data setup
        List<GrantedAuthority> regMemberAuthorities = AuthorityUtils.createAuthorityList("REGISTRATIONSERVMEMBER");
        List<GrantedAuthority> secretaryAuthorities = AuthorityUtils.createAuthorityList("SECRETARY");

        when(userDetailsService.loadUserByUsername("230000@anas.ac.be")).thenReturn(new User("Joe", "1234", regMemberAuthorities));
        when(userDetailsService.loadUserByUsername("230001@anas.ac.be")).thenReturn(new User("Jack", "1234", secretaryAuthorities));

        regToken = tokenService.generateJwt("Joe", regMemberAuthorities);
        secrToken = tokenService.generateJwt("Jack", secretaryAuthorities);
    }

    @Test
    void getAStudentShouldSuccessWhenTargetExists() throws Exception {
        var student = new StudentData();
        var studentDTO = new StudentDTO(
                "Test",
                "",
                230000,
                "",
                "230000@nasa.ac.be",
                RegistrationStatus.InProgress,
                "CursusId"
        );

        when(studentService.loadStudent(anyInt())).thenReturn(student);
        when(studentDTOMapper.apply(any())).thenReturn(studentDTO);

        mvc
                .perform(
                        get("/api/v1/students/{matricule}", 230000)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    void registerNewStudentShouldSuccess() throws Exception {
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

        var student = new StudentData();

        when(studentService.registerStudent(any(), any())).thenReturn(student);

        mvc
                .perform(
                        post("/api/v1/students/add?cursusId={curId}", "CursusID")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void registerNewStudentShouldFailWhenRegisterStudentFail() throws Exception {
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

        when(studentService.registerStudent(any(), any())).thenReturn(null);

        mvc
                .perform(
                        post("/api/v1/students/add?cursusId={curId}", "CursusID")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something went wrong :("));
    }

    @Test
    void registerNewStudentShouldFailIfNotAuthorized() throws Exception {
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

        mvc
                .perform(
                        post("/api/v1/students/add?cursusId={curId}", "CursusID")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andExpect(status().isForbidden());
    }
}