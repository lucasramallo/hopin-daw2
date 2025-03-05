package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.CabNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.util.DriverValidations;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverService {
    @Autowired
    private DriverRepository repository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CabService cabService;

    public DriverResponseDTO createDriver(CreateDriverRequestDTO requestDTO) {
        DriverValidations.validateName(requestDTO.name());
        DriverValidations.validateEmail(requestDTO.email());
        DriverValidations.validateAge(requestDTO.dateOfBirth());

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setName(requestDTO.name());
        driver.setPassword(requestDTO.password());
        driver.setDateOfBirth(requestDTO.dateOfBirth());
        driver.setCreatedAt(LocalDateTime.now());
        driver.setEmail(requestDTO.email());

        Cab cab = cabService.createCab(requestDTO);

        driver.setCab(cab);

        repository.save(driver);

        return new DriverResponseDTO(
                driver.getId(),
                driver.getName(),
                driver.getEmail(),
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
                driverFound.get().getEmail(),
                driverFound.get().getDateOfBirth(),
                driverFound.get().getCab(),
                driverFound.get().getCreatedAt()
        );
    }

    public List<TripResponseDTO> getTripsHistory(UUID customerId, int page,  int size) {
        Optional<Driver> driver = repository.findById(customerId);

        if(driver.isEmpty()) {
            throw new DriverNotFoundException();
        }

        ArrayList<Trip> trips = tripRepository.findAllByDriverId(driver.get().getId(), PageRequest.of(page, size));

        return trips.stream()
                .map(TripMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DriverResponseDTO editDriver(UUID id, EditDriverRequestDTO requestDTO) {
        DriverValidations.validateName(requestDTO.name());
        DriverValidations.validateAge(requestDTO.dateOfBirth());
        DriverValidations.validateEmail(requestDTO.email());

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
                driverFound.get().getEmail(),
                driverFound.get().getDateOfBirth(),
                driverFound.get().getCab(),
                driverFound.get().getCreatedAt()
        );
    }

    public Cab getDriverCab(UUID driverId) {
        Optional<Cab> cab = repository.findCabByDriverId(driverId);

        if(cab.isEmpty()) {
            throw new CabNotFoundException();
        }

        return cab.get();
    }

}
