package com.anae.api.Mapper;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.Entities.Cursus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CursusDTOMapper implements Function<Cursus, CursusDTO> {
    @Override
    public CursusDTO apply(Cursus cursus) {
        return new CursusDTO(
                cursus.getCursusId(),
                cursus.getTitle()
        );
    }
}
