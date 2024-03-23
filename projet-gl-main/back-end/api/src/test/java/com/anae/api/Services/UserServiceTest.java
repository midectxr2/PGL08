package com.anae.api.Services;

import com.anae.api.Entities.Course;
import com.anae.api.Entities.Role;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.UserDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserDataRepository userDataRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(encoder, userDataRepository);
    }

    @AfterEach
    void tearDown() {
        userDataRepository.deleteAll();
    }

    @Test
    void loadUserByUsername_ValidUser_ShouldReturnUserDetails() {
        //given
        String schoolEmail = "test@example.com";
        UserData user = new UserData();
        user.setFirstname("testUser");
        user.setSchoolEmail(schoolEmail);
        when(userDataRepository.findBySchoolEmail(schoolEmail)).thenReturn(Optional.of(user));

        //when
        var userDetails = userService.loadUserByUsername(schoolEmail);

        //then
        assertThat(userDetails).isEqualTo(user);
    }

    @Test
    void loadUserByUsername_InvalidUser_ShouldThrowUsernameNotFoundException() {
        //given
        String schoolEmail = "test@example.com";
        when(userDataRepository.findBySchoolEmail(schoolEmail)).thenReturn(Optional.empty());
        //when

        //then
        assertThatThrownBy(() -> userService.loadUserByUsername(schoolEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("user is not valid");
    }
    @Test
    void getTeacher_WhenTarget_Exists() {
        var teacherRole = new Role();
        teacherRole.setAuthority(UserRole.TEACHER);

        var teacher = new UserData();
        teacher.setAuthorities(Set.of(teacherRole));

        when(userDataRepository.findByMatriculeAndAuthoritiesAuthority(230000, UserRole.TEACHER)).thenReturn(Optional.of(teacher));

        UserData fetchedTeacher = userService.getTeacher(230000);

        assertThat(fetchedTeacher).isEqualTo(teacher);
    }

    @Test
    void throwException_WhenTarget_DoesntExist() {
        assertThatThrownBy(() -> userService.getTeacher(230000))
                .isInstanceOf(NoSuchElementException.class);

        verify(userDataRepository).findByMatriculeAndAuthoritiesAuthority(anyInt(), any());
    }

    @Test
    void affectTeacher_shouldSuccess_WhenSaveTeacher_GetNoIssue(){
        var teacherRole = new Role();
        teacherRole.setAuthority(UserRole.TEACHER);

        var teacher = new UserData();
        teacher.setAuthorities(Set.of(teacherRole));
        teacher.setTaughtCourses(new HashSet<>());

        var course = new Course();
        course.setCourseId("testCourseId");
        course.setTeachers(new HashSet<>());

        when(userDataRepository.findByMatriculeAndAuthoritiesAuthority(230000, UserRole.TEACHER)).thenReturn(Optional.of(teacher));

        boolean result = userService.affectTeacher(230000, course);

        verify(userDataRepository).save(teacher);
        assertThat(result).isTrue();
    }

    @Test
    void affectTeacher_shouldFail_WhenSaveTeacher_GetIssue(){
        var teacherRole = new Role();
        teacherRole.setAuthority(UserRole.TEACHER);

        var teacher = new UserData();
        teacher.setAuthorities(Set.of(teacherRole));
        teacher.setTaughtCourses(new HashSet<>());

        var course = new Course();
        course.setCourseId("testCourseId");
        course.setTeachers(new HashSet<>());

        when(userDataRepository.findByMatriculeAndAuthoritiesAuthority(230000, UserRole.TEACHER)).thenReturn(Optional.of(teacher));
        when(userDataRepository.save(teacher)).thenThrow(IllegalArgumentException.class);

        boolean result = userService.affectTeacher(230000, course);

        verify(userDataRepository).save(teacher);
        assertThat(result).isFalse();
    }


    @Test
    void loadUser_ShouldUserRepositoryFunction() {
        var user = new UserData();
        user.setFirstname("Test");

        when(userDataRepository.findByMatricule(230000)).thenReturn(Optional.of(user));

        var fetchedUser = userService.load(230000);

        verify(userDataRepository).findByMatricule(anyInt());

        assertThat(fetchedUser).isEqualTo(user);
    }

    @Test
    void shouldSave_ByUse_RepositorySaveFunction() {
        var user = new UserData();
        user.setFirstname("Joe");

        userService.save(user);

        verify(userDataRepository).save(user);
    }

}