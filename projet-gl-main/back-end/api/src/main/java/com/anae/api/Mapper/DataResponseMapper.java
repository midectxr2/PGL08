package com.anae.api.Mapper;

import com.anae.api.DTOs.UserDataResponse;
import com.anae.api.Entities.UserData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataResponseMapper implements Function<UserData, UserDataResponse> {
    private final AddressDTOMapper addressDTOMapper;

    public DataResponseMapper(AddressDTOMapper addressDTOMapper) {
        this.addressDTOMapper = addressDTOMapper;
    }

    @Override
    public UserDataResponse apply(UserData user) {
        return new UserDataResponse(
                user.getFirstname(),
                user.getLastname(),
                user.getMatricule(),
                user.getPrivateEmail(),
                user.getSchoolEmail(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                addressDTOMapper.apply(user.getAdress())

        );
    }
}
