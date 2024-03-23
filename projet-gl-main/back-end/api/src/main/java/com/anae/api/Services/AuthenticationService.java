package com.anae.api.Services;

import com.anae.api.DTOs.LoginResponse;
import com.anae.api.DTOs.RegistrationDTO;
import com.anae.api.DTOs.RegistrationResponse;
import com.anae.api.Entities.Address;
import com.anae.api.Entities.Role;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.RoleRepository;
import com.anae.api.Repository.UserDataRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    private final UserDataRepository userDataRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;


    public AuthenticationService(TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserDataRepository userDataRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDataRepository = userDataRepository;
    }

    public RegistrationResponse registerUser(RegistrationDTO userData, UserRole role) {

        String encodedPassword = passwordEncoder.encode(userData.password());
        Role userRole = roleRepository.findByAuthority(role).orElseThrow();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        var address = new Address();
        address.setAdress(userData.address());
        address.setAdress2(userData.address2());
        address.setCity(userData.city());
        address.setCountry(userData.country());
        address.setPhonenumber(userData.phonenumber());
        address.setPostalCode(userData.postal_code());
        address.setRegion(userData.region());


        var user = userDataRepository
                .save(
                        new UserData(
                                0,
                                userData.firstname(),
                                userData.lastname(),
                                encodedPassword,
                                userData.email(),
                                authorities,
                                address
                        )
                );

        return new RegistrationResponse(user.getSchoolEmail());
    }

    public LoginResponse loginUser(String schoolEmail, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(schoolEmail, password)
        );

        String token = tokenService.generateJwt(auth);
        UserData user = userDataRepository.findBySchoolEmail(schoolEmail).orElseThrow();

        return new LoginResponse (
                user.getMatricule(),
                token
        );
    }

}
