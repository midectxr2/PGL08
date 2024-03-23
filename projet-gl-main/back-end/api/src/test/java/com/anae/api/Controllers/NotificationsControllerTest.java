package com.anae.api.Controllers;

import com.anae.api.DTOs.DirectUserNotificationDTO;
import com.anae.api.Entities.DirectUserNotification;
import com.anae.api.Repository.DirectUserNotificationRepository;
import com.anae.api.Services.TokenService;
import com.anae.api.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserService userDetailsService;

    @MockBean
    private DirectUserNotificationRepository directUserNotificationRepository;

    @Autowired
    @InjectMocks
    private NotificationsController notificationsController;

    private String adminToken;

    @BeforeEach
    public void setUp() {
        // Mock data setup
        List<GrantedAuthority> adminAuthorities = AuthorityUtils.createAuthorityList("ADMIN");

        when(userDetailsService.loadUserByUsername("230000@anas.ac.be")).thenReturn(new User("Joe", "1234", adminAuthorities));

        adminToken = tokenService.generateJwt("Jack", adminAuthorities);
    }

    @Test
    void getDirectNotificationsShouldSuccess() throws Exception {
        var directNotification = new DirectUserNotification();
        directNotification.setMessage("Hello world");

        when(directUserNotificationRepository.findAllByUserDataMatricule(230000)).thenReturn(Optional.of(List.of(directNotification)));

        mvc
                .perform(
                        get("/api/v1/notifs/{matricule}", 230000)
                                .header("AUTHORIZATION", "Bearer " + adminToken)

                )
                .andExpect(status().isOk());

    }

    @Test
    void clearDirectNotificationsShouldSuccess() throws Exception {
        mvc
                .perform(
                        get("/api/v1/notifs/{matricule}/clear", 230000)
                                .header("AUTHORIZATION", "Bearer " + adminToken)

                )
                .andExpect(status().isOk())
                .andExpect(content().string("cleared"));

    }

    @Test
    void clearDirectNotificationsShouldFailOnMatricleNull() throws Exception {
        mvc
                .perform(
                        get("/api/v1/notifs/null/clear")
                                .header("AUTHORIZATION", "Bearer " + adminToken)

                )
                .andExpect(status().isBadRequest());

    }
}