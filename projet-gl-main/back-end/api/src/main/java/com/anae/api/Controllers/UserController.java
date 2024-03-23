package com.anae.api.Controllers;


import com.anae.api.DTOs.ModifiedDataDTO;
import com.anae.api.DTOs.UserDataResponse;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import com.anae.api.Mapper.DataResponseMapper;
import com.anae.api.Repository.UserDataRepository;
import com.anae.api.Users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserDataRepository userDataRepository;

    private final DataResponseMapper dataResponseMapper;

    public UserController(UserDataRepository userDataRepository, DataResponseMapper dataResponseMapper) {
        this.userDataRepository = userDataRepository;
        this.dataResponseMapper = dataResponseMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDataResponse>> getUsersData(
            @RequestParam Optional<UserRole> userRole) {
        return userRole.isEmpty() ?
                ResponseEntity.ok(
                        userDataRepository.findAll()
                                .stream()
                                .map(dataResponseMapper)
                                .toList()
                ) :
                ResponseEntity.ok(
                        userDataRepository.findByAuthoritiesAuthority(userRole)
                                .stream()
                                .map(dataResponseMapper)
                                .toList()
                );
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<UserDataResponse> getUserData(@PathVariable Integer matricule) {
        User user = new User(matricule, userDataRepository);

        return ResponseEntity.ok(dataResponseMapper.apply(user.getUserData()));
    }

    @PatchMapping(path = "/{matricule}", produces = "application/json")
    public ResponseEntity<String> modifyUserData(@PathVariable("matricule") int matricule, @RequestBody ModifiedDataDTO modifiedUserData) throws IllegalArgumentException, IllegalAccessException {


        User user = new User(matricule, userDataRepository);

        if (user.getUserData() == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        } else {
            user.modifyPersonalData(modifiedUserData);
            return ResponseEntity.ok("Personnal Data Modified");
        }
    }
}
