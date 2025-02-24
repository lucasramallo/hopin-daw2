package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.data.jpa.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CabService {
    @Autowired
    private CabRepository repository;

    public Cab createCab(CreateDriverRequestDTO requestDTO) {
        Cab cab = new Cab();
        cab.setId(UUID.randomUUID());
        cab.setModel(requestDTO.model());
        cab.setPlateNum(requestDTO.plateNum());
        cab.setColor(requestDTO.color());

        repository.save(cab);

        return cab;
    }
}
