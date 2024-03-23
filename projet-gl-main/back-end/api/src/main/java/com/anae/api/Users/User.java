package com.anae.api.Users;

import com.anae.api.DTOs.ModifiedDataDTO;
import com.anae.api.Entities.Address;
import com.anae.api.Entities.UserData;
import com.anae.api.Mapper.AddressMapper;
import com.anae.api.Repository.UserDataRepository;
import lombok.Getter;

import java.lang.reflect.Field;


public class User {

    private final AddressMapper addressMapper;
    @Getter
    protected UserData userData;
    protected UserDataRepository userDataRepository;

    public User(int matricule, UserDataRepository userDataRepository) {
        this.addressMapper = new AddressMapper();
        this.userDataRepository = userDataRepository;
        this.userData = userDataRepository.findByMatricule(matricule).orElse(null);

    }

    public boolean modifyPersonalData(ModifiedDataDTO modifiedUserData) throws IllegalArgumentException {

        try {
            if (modifiedUserData.privateEmail() != null)
                userData.setPrivateEmail(modifiedUserData.privateEmail());

            Address modifiedAddress = addressMapper.apply(modifiedUserData.address());

            Field[] fields = Address.class.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                Object value = field.get(modifiedAddress);

                if (value != null) {
                    field.set(userData.getAdress(), value);
                }
            }

            userDataRepository.save(userData);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
