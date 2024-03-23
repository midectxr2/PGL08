package com.anae.api.Controllers;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.Entities.Cursus;
import com.anae.api.Mapper.CursusDTOMapper;
import com.anae.api.Services.CursusService;
import com.anae.api.Services.TokenService;
import com.anae.api.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CursusControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    @Autowired
    @InjectMocks
    private CursusController cursusController;

    @MockBean
    private CursusService cursusService;


    @MockBean
    private CursusDTOMapper cursusDTOMapper;

    private String regToken, secrToken, teachToken, stuToken;

    @BeforeEach
    public void setUp() {
        Cursus cursus = new Cursus();
        CursusDTO cursusDTO = new CursusDTO("cursusId", "Title 1");
        List<Cursus> cursusList = List.of(cursus);

        cursus.setCursusId(cursusDTO.id());
        cursus.setTitle(cursusDTO.title());


        // Mock data setup
        List<GrantedAuthority> regMemberAuthorities = AuthorityUtils.createAuthorityList("REGISTRATIONSERVMEMBER");
        List<GrantedAuthority> secretaryAuthorities = AuthorityUtils.createAuthorityList("SECRETARY");
        List<GrantedAuthority> teacherAuthorities = AuthorityUtils.createAuthorityList("TEACHER");
        List<GrantedAuthority> studentAuthorities = AuthorityUtils.createAuthorityList("STUDENT");

        // Mocking service methods
        when(cursusService.getAll()).thenReturn(cursusList);
        when(cursusService.loadCursus("cursusId")).thenReturn(cursus);
        when(cursusService.addCourse(any(Cursus.class), anyString())).thenReturn(true);

        when(cursusDTOMapper.apply(any())).thenReturn(cursusDTO);

        when(userDetailsService.loadUserByUsername("230000@anas.ac.be")).thenReturn(new User("Joe", "1234", regMemberAuthorities));
        when(userDetailsService.loadUserByUsername("230001@anas.ac.be")).thenReturn(new User("Jack", "1234", secretaryAuthorities));
        when(userDetailsService.loadUserByUsername("230002@anas.ac.be")).thenReturn(new User("William", "1234", teacherAuthorities));
        when(userDetailsService.loadUserByUsername("230003@anas.ac.be")).thenReturn(new User("Averell", "1234", studentAuthorities));



        regToken = tokenService.generateJwt("Joe", regMemberAuthorities);
        secrToken = tokenService.generateJwt("Jack", secretaryAuthorities);
        teachToken = tokenService.generateJwt("William", teacherAuthorities);
        stuToken = tokenService.generateJwt("Averell", studentAuthorities);
    }

    @Test
    void shouldFailGetAllWith401_Unauth() throws Exception {
        mvc
                .perform(
                        get("/api/v1/cursus")
                                .with(anonymous())
                )
                .andExpect(status().isUnauthorized());

        verify(cursusService, never()).getAll();
    }

    @Test
    void shouldGetAllWith200_OK() throws Exception {

        mvc
                .perform(
                        get("/api/v1/cursus")
                                .header("AUTHORIZATION", "Bearer " + stuToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetTheCursusWith200_OK() throws Exception {
        mvc
                .perform(
                        get("/api/v1/cursus/{id}", "cursusId")
                                .header("AUTHORIZATION", "Bearer " + stuToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldResponseForbiddenIfStudentTryAddCursus() throws Exception {

        mvc
                .perform(
                        post("/api/v1/cursus/add")
                                .content(asJsonString(new CursusDTO("id", "title")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + teachToken)
                )
                .andExpect(status().isForbidden());

        verify(cursusService, never()).existById(anyString());
    }

    @Test
    void shouldGetA201IfSecretaryAddNewCursus() throws Exception {
        when(cursusService.existById(anyString())).thenReturn(false);

        mvc
                .perform(
                        post("/api/v1/cursus/add")
                                .content(asJsonString(new CursusDTO("id", "title")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Cursus registered"));
    }

    @Test
    void shouldGetA200IfSecretaryTryAddCursusThatAlreadyExists() throws Exception {
        when(cursusService.existById(anyString())).thenReturn(true);

        mvc
                .perform(
                        post("/api/v1/cursus/add")
                                .content(asJsonString(new CursusDTO("id", "title")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cursus already exists"));

        verify(cursusService, never()).registerCursus(any());
    }

    @Test
    void shouldGet200WhenSecretaryModifySuccessfullyCursusInfo() throws Exception {
        mvc
                .perform(
                        post("/api/v1/cursus/{cursusId}/change?newId=newTest&newTitle=newTitle", "cursusId")
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Change applied"));
    }

    @Test
    void shouldGet200WhenSecretaryAddNewCourseToCursus() throws Exception {
        mvc.
                perform(
                        post(
                                "/api/v1/cursus/{cursusId}/add?courseId={courseId}"
                                , "cursusId", "courseId")
                                .header("AUTHORIZATION", "Bearer "+secrToken)

                )
                .andExpect(status().isOk())
                .andExpect(content().string("Course added to cursus: cursusId"));
    }

    @Test
    void shouldSuccessWhenSecretaryAddNewCourseToCursus() throws Exception {
        mvc.
                perform(
                        post(
                                "/api/v1/cursus/{cursusId}/add?courseId={courseId}"
                                , "cursusId", "courseId")
                                .header("AUTHORIZATION", "Bearer "+secrToken)

                )
                .andExpect(status().isOk())
                .andExpect(content().string("Course added to cursus: cursusId"));
    }

    @Test
    void shouldGet400IfCourseFailedToBeRegisteredInCursus() throws Exception {
        when(cursusService.addCourse(any(), any())).thenReturn(false);

        mvc.
                perform(
                        post(
                                "/api/v1/cursus/{cursusId}/add?courseId={courseId}"
                                , "cursusId", "courseId")
                                .header("AUTHORIZATION", "Bearer "+secrToken)

                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Verify given information"));
    }

    @Test
    void shouldGet200IfCourseSuccessToBeRemovedFromCursus() throws Exception {
        when(cursusService.removeCourse(any(), any())).thenReturn(true);

        mvc.
                perform(
                        post(
                                "/api/v1/cursus/{cursusId}/remove?courseId={courseId}"
                                , "cursusId", "courseId")
                                .header("AUTHORIZATION", "Bearer "+secrToken)

                )
                .andExpect(status().isOk())
                .andExpect(content().string("Course removed from cursus: cursusId"));
    }

    @Test
    void shouldGet400IfCourseFailedToBeRemovedFromCursus() throws Exception {
        when(cursusService.removeCourse(any(), any())).thenReturn(false);

        mvc.
                perform(
                        post(
                                "/api/v1/cursus/{cursusId}/remove?courseId={courseId}"
                                , "cursusId", "courseId")
                                .header("AUTHORIZATION", "Bearer "+secrToken)

                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Verify given information"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}