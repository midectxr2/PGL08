package com.anae.api.Services;

import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.Entities.Role;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.RoleRepository;
import com.anae.api.Repository.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(tokenService, authenticationManager, passwordEncoder, roleRepository, userDataRepository);
    }

    @Test
    void registerUser() {
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

        var user = new UserData();
        user.setSchoolEmail("230000@nasa.ac.be");

        var role = new Role();
        role.setAuthority(UserRole.ADMIN);

        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");
        when(roleRepository.findByAuthority(any())).thenReturn(Optional.of(role));
        when(userDataRepository.save(any())).thenReturn(user);

        authenticationService.registerUser(registrationData, UserRole.ADMIN);

        verify(userDataRepository).save(any());
    }

    @Test
    void loginUser() {
        Authentication authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);

        var user = new UserData();
        user.setSchoolEmail("230000@nana.ac.be");
        user.setFirstname("Andy");
        user.setMatricule(230000);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateJwt(any())).thenReturn("aGoodToken");
        when(userDataRepository.findBySchoolEmail("230000@nasa.ac.be")).thenReturn(Optional.of(user));

        authenticationService.loginUser("230000@nasa.ac.be", "password");

        verify(authenticationManager).authenticate(any());
        verify(tokenService).generateJwt(authentication);
        verify(userDataRepository).findBySchoolEmail("230000@nasa.ac.be");
    }
}