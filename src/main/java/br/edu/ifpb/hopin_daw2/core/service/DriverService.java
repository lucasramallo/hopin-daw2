package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.DriverResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.EditDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.util.DriverValidations;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {
    @Autowired
    private DriverRepository repository;

    @Autowired
    private CabService cabService;

    public DriverResponseDTO createDriver(CreateDriverRequestDTO requestDTO) {
        DriverValidations.execute(requestDTO);

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setName(requestDTO.name());
        driver.setPassword(requestDTO.password());
        driver.setDateOfBirth(requestDTO.dateOfBirth());
        driver.setCreatedAt(LocalDateTime.now());

        Cab cab = cabService.createCab(requestDTO);

        driver.setCab(cab);

        repository.save(driver);

        return new DriverResponseDTO(
                driver.getId(),
                driver.getName(),
                driver.getDateOfBirth(),
                driver.getCab(),
                driver.getCreatedAt()
        );
    }

    public DriverResponseDTO getDriverById(UUID id) {
        Optional<Driver> driverFound = repository.findById(id);

        if(driverFound.isEmpty()) {
            throw new DriverNotFoundException();
        }

        return new DriverResponseDTO(
                driverFound.get().getId(),
                driverFound.get().getName(),
                driverFound.get().getDateOfBirth(),
                driverFound.get().getCab(),
                driverFound.get().getCreatedAt()
        );
    }

    public DriverResponseDTO editDriver(UUID id, EditDriverRequestDTO requestDTO) {
        DriverValidations.validateName(requestDTO.name());
        DriverValidations.validateAge(requestDTO.dateOfBirth());

        Optional<Driver> driverFound = repository.findById(id);

        if(driverFound.isEmpty()) {
            throw new DriverNotFoundException();
        }

        driverFound.get().setName(requestDTO.name());
        driverFound.get().setDateOfBirth(requestDTO.dateOfBirth());

        repository.save(driverFound.get());

        return new DriverResponseDTO(
                driverFound.get().getId(),
                driverFound.get().getName(),
                driverFound.get().getDateOfBirth(),
                driverFound.get().getCab(),
                driverFound.get().getCreatedAt()
        );
    }
}
