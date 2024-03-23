package com.anae.api.Controllers;

import com.anae.api.DTOs.LoginDTO;
import com.anae.api.DTOs.LoginResponse;
import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.DTOs.RegistrationResponse;
import com.anae.api.Enums.UserRole;
import com.anae.api.Services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @RequestParam UserRole role,
            @RequestBody RegistrationDTO body
    ) {
        if(role == UserRole.STUDENT)
            return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(authenticationService.registerUser(body, role), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDTO body) {
        var userLoginResponse = authenticationService.loginUser(body.email(), body.password());
        return ResponseEntity.ok(userLoginResponse);
    }

    @RequestMapping("/validation")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Token validated");
    }
}
