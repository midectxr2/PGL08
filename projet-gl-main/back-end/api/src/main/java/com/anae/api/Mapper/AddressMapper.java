package com.anae.api.Mapper;

import com.anae.api.DTOs.AddressDTO;
import com.anae.api.Entities.Address;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AddressMapper implements Function<AddressDTO, Address> {
    @Override
    public Address apply(AddressDTO addressDTO) {
        return new Address(
                addressDTO.city(),
                addressDTO.adress(),
                addressDTO.adress2(),
                addressDTO.region(),
                addressDTO.postalCode(),
                addressDTO.phonenumber(),
                addressDTO.country()
        );
    }
}
