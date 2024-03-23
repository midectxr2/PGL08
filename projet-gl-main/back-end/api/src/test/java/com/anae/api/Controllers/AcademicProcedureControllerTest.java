package com.anae.api.Controllers;

import com.anae.api.DTOs.AcademicProcedureDTO;
import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.DTOs.StudentDTO;
import com.anae.api.Entities.AcademicProcedure;
import com.anae.api.Entities.StudentData;
import com.anae.api.Enums.ProcedureType;
import com.anae.api.Enums.RegistrationStatus;
import com.anae.api.Mapper.AcademicProcedureDTOMapper;
import com.anae.api.Services.AcademicProcedureService;
import com.anae.api.Services.StudentService;
import com.anae.api.Services.TokenService;
import com.anae.api.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AcademicProcedureControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    @Autowired
    @InjectMocks
    private AcademicProcedureController academicProcedureController;

    @MockBean
    private AcademicProcedureService academicProcedureService;

    @MockBean
    private AcademicProcedureDTOMapper academicProcedureDTOMapper;

    @MockBean
    private StudentService studentService;

    private String regToken, secrToken, teachToken, stuToken;

    @BeforeEach
    public void setUp() {
        // Mock data setup
        List<GrantedAuthority> regMemberAuthorities = AuthorityUtils.createAuthorityList("REGISTRATIONSERVMEMBER");
        List<GrantedAuthority> secretaryAuthorities = AuthorityUtils.createAuthorityList("SECRETARY");
        List<GrantedAuthority> teacherAuthorities = AuthorityUtils.createAuthorityList("TEACHER");
        List<GrantedAuthority> studentAuthorities = AuthorityUtils.createAuthorityList("STUDENT");


        when(userDetailsService.loadUserByUsername("230000@anae.ac.be")).thenReturn(new User("Joe", "1234", regMemberAuthorities));
        when(userDetailsService.loadUserByUsername("230001@anae.ac.be")).thenReturn(new User("Jack", "1234", secretaryAuthorities));
        when(userDetailsService.loadUserByUsername("230002@anae.ac.be")).thenReturn(new User("William", "1234", teacherAuthorities));
        when(userDetailsService.loadUserByUsername("230003@anae.ac.be")).thenReturn(new User("Averell", "1234", studentAuthorities));


        regToken = tokenService.generateJwt("Joe", regMemberAuthorities);
        secrToken = tokenService.generateJwt("Jack", secretaryAuthorities);
        teachToken = tokenService.generateJwt("William", teacherAuthorities);
        stuToken = tokenService.generateJwt("Averell", studentAuthorities);
    }

    @Test
    void shouldGet202WhenStudentSendProcedure() throws Exception {

        mvc
                .perform(
                        post("/api/v1/procedures/{matricule}/send?type={type}", 230010, "Registration")
                                .header("AUTHORIZATION", "Bearer " + stuToken)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string("Your request has been saved"));

        //then

        verify(academicProcedureService).sendProcedure(230010, ProcedureType.Registration);
    }

    @Test
    void shouldGet403IdAnotherMemberTrySendProcedure() throws Exception {
        mvc
                .perform(
                        post("/api/v1/procedures/{matricule}/send?type={type}", 230010, "Registration")
                                .header("AUTHORIZATION", "Bearer " + teachToken)
                )
                .andDo(print())
                .andExpect(status().isForbidden());

        //then

        verify(academicProcedureService, never()).sendProcedure(anyInt(), any(ProcedureType.class));
    }

    @Test
    void shouldSuccessToGetAllProcedures() throws Exception {
        var academicProcedure = new AcademicProcedure();
        academicProcedure.setType(ProcedureType.Unregister);

        var studentDTO = new StudentDTO("Andy", "Test", 230000, "test@example.com", "230000@anas.ac.be", RegistrationStatus.InProgress, "CursusId");

        when(academicProcedureService.getAll()).thenReturn(List.of(academicProcedure));
        when(academicProcedureDTOMapper.apply(any())).thenReturn(new AcademicProcedureDTO(ProcedureType.Unregister, studentDTO));

        mvc
                .perform(
                        get("/api/v1/procedures")
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailToGetAllProceduresAndGet403() throws Exception {
        mvc
                .perform(
                        get("/api/v1/procedures")
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andDo(print())
                .andExpect(status().isForbidden());

        verify(academicProcedureService, never()).getAll();
    }

    @Test
    void shouldSuccessToSendResponse() throws Exception {
        mvc
                .perform(
                        post("/api/v1/procedures/sendResponse")
                                .content(asJsonString(new DirectUserNotificationDTO(230010, "Hello world")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Student has been notified"));
    }

    @Test
    void deleteProcedure() throws Exception {
        //given
        var procedure = new AcademicProcedure();
        procedure.setType(ProcedureType.Unregister);

        var student = new StudentData();
        student.setProcedure(procedure);

        procedure.setStudentData(student);

        when(academicProcedureService.get(anyInt())).thenReturn(procedure);

        mvc
                .perform(
                delete("/api/v1/procedures/{id}", 42)
                        .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));


        ArgumentCaptor<StudentData> studentDataArgumentCaptor = ArgumentCaptor.forClass(StudentData.class);

        verify(studentService).save(studentDataArgumentCaptor.capture());

        var capturedStudent = studentDataArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
        assertThat(capturedStudent.getProcedure()).isNull();
    }
}