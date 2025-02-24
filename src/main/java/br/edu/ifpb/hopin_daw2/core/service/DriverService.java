package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.util.DriverValidations;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DriverService {
    @Autowired
    private DriverRepository repository;

    @Autowired
    private CabService cabService;

    public void createDriver(CreateDriverRequestDTO requestDTO) {
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setName(requestDTO.name());
        driver.setPassword(requestDTO.password());
        driver.setDateOfBirth(requestDTO.dateOfBirth());
        driver.setCreatedAt(LocalDateTime.now());

        Cab cab = cabService.createCab(requestDTO);

        driver.setCab(cab);

        repository.save(driver);
    }
}
