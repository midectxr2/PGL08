package com.anae.api.Controllers;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.DTOs.LoginDTO;
import com.anae.api.DTOs.LoginResponse;
import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.Entities.Cursus;
import com.anae.api.Services.AuthenticationService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    @InjectMocks
    private AuthenticationController authenticationController;

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
    void shouldCreateANewUser() throws Exception {

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
                        post("/api/v1/auth/register?role={role}", "TEACHER")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isCreated());

        verify(authenticationService).registerUser(any(), any());
    }

    @Test
    void shouldFail_OnTryAddNewUser_WithNonValidAuthority() throws Exception {

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
                        post("/api/v1/auth/register?role={role}", "TEACHER")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + secrToken)
                )
                .andExpect(status().isForbidden());

        verify(authenticationService, never()).registerUser(any(), any());
    }

    @Test
    void shouldFail_OnTryAddStudent_ByAuthenticationController() throws Exception {

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
                        post("/api/v1/auth/register?role={role}", "STUDENT")
                                .content(asJsonString(registrationData))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("AUTHORIZATION", "Bearer " + regToken)
                )
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).registerUser(any(), any());
    }

    @Test
    void shouldResponseTokenAndMatriculeOnCorrectLogData() throws Exception {
        var loginInfo = new LoginDTO("example@ex.be", "1234");

        when(authenticationService.loginUser(anyString(), anyString())).thenReturn(new LoginResponse(230000, secrToken));

        mvc
                .perform(
                        post("/api/v1/auth/login")
                                .content(asJsonString(loginInfo))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(authenticationService).loginUser("example@ex.be", "1234");
    }

    @Test
    void shouldSuccessOnValidToken() throws Exception {

        mvc
                .perform(
                        post("/api/v1/auth/validation")
                                .header("AUTHORIZATION", "Bearer " + secrToken)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailOnExpiredToken() throws Exception {
        var token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiSmFjayIsImlhdCI6MTcxMDY5MzY2NCwicm9sZXMiOiJTRUNSRVRBUlkifQ.esKuNRgd5HCr6lVpU2LTP2XmR654xR61khpz7uWTKKgwXlTK03mgw94Jfr59ZCrMez6Iso1bANg2qzrZbxPeoI6ZcEMGtwKxYaUMWx01RaVPUuQmNyrAm4fUzpJa1uzmdmDB2A1K30U38_0y1TtqHzPuJGuZyE78LWJ2Vv_LDE3GhRMxtilVvDoAAupPMXiGRpNcLspe2KhtaNraC6mQUC2ZlYMIpR8Dr86KvPnaBg9zrEe1qke11TY3wPIkBk8fonSTGjuKqrLSc2pn6UvlUxE5K_a8d4Vewu-Z_cuwUFix5f38zQIusjga3MOrjlVgBYVJ-EPCkNR5J5DmhjLM1Q";

        mvc
                .perform(
                        post("/api/v1/auth/validation")
                                .header("AUTHORIZATION", "Bearer " + token)

                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}