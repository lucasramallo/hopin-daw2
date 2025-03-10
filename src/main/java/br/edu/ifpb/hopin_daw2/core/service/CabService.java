package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.EditDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.CabNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.InvalidPlateException;
import br.edu.ifpb.hopin_daw2.data.jpa.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CabService {
    @Autowired
    private CabRepository repository;

    public Cab createCab(CreateDriverRequestDTO requestDTO) {
        validatePlate(requestDTO.plateNum());

        Cab cab = new Cab();
        cab.setModel(requestDTO.model());
        cab.setPlateNum(requestDTO.plateNum());
        cab.setColor(requestDTO.color());

        repository.save(cab);

        return cab;
    }

    public Cab editCab(EditDriverRequestDTO requestDTO) {
        validatePlate(requestDTO.plateNum());
        Optional<Cab> cab = repository.findByPlateNum(requestDTO.plateNum());

        if(cab.isEmpty()) {
            throw new CabNotFoundException();
        }

        Cab cabToEdit = cab.get();
        cabToEdit.setModel(requestDTO.model());
        cabToEdit.setPlateNum(requestDTO.plateNum());
        cabToEdit.setColor(requestDTO.color());

        repository.save(cabToEdit);

        return cabToEdit;
    }

    public void validatePlate(String plate) {
        Pattern pattern = Pattern.compile("[A-Z]{3}-[0-9]{4}");
        Matcher matcher = pattern.matcher(plate);

        if(!matcher.matches()) {
            throw new InvalidPlateException("Invalid Plate!");
        }
    }
}
