package com.anae.api.Mapper;

import com.anae.api.DTOs.AddressDTO;
import com.anae.api.Entities.Address;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AddressDTOMapper implements Function<Address, AddressDTO> {
    @Override
    public AddressDTO apply(Address address) {
        return new AddressDTO(
                address.getCity(),
                address.getAdress(),
                address.getAdress2(),
                address.getRegion(),
                address.getPostalCode(),
                address.getPhonenumber(),
                address.getCountry()
        );
    }
}
