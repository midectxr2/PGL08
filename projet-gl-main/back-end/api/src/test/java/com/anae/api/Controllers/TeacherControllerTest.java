package com.anae.api.Controllers;

import com.anae.api.Entities.Course;
import com.anae.api.Services.CourseService;
import com.anae.api.Services.TokenService;
import com.anae.api.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    @MockBean
    private CourseService courseService;

    @Autowired
    @InjectMocks
    private TeacherController teacherController;

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
    void affectTeacherToCourseSuccess_WhenTeacherExists() throws Exception{
        var course = new Course();
        course.setTeachers(new HashSet<>());

        when(courseService.getCourse(any())).thenReturn(course);
        when(userDetailsService.affectTeacher(anyInt(), any())).thenReturn(true);

        mvc
                .perform(
                        post("/api/v1/teachers/{matricule}/affect?courseId={cId}", 230000, "INFO-22")
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Teacher 230000 Affected to course: INFO-22"));
    }

    @Test
    void affectTeacherToCourseFail_WhenTeacherDoesntExist() throws Exception{
        var course = new Course();
        course.setTeachers(new HashSet<>());

        when(courseService.getCourse(any())).thenReturn(course);
        when(userDetailsService.affectTeacher(anyInt(), any())).thenReturn(false);

        mvc
                .perform(
                        post("/api/v1/teachers/{matricule}/affect?courseId={cId}", 230000, "INFO-22")
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Teacher not found"));
    }

    @Test
    void affectTeacherToCourseFail_IfUserNotAllowed() throws Exception{
        mvc
                .perform(
                        post("/api/v1/teachers/{matricule}/affect?courseId={cId}", 230000, "INFO-22")
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isForbidden());
    }
}