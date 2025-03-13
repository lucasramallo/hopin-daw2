package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.CabNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.util.DriverValidations;
import br.edu.ifpb.hopin_daw2.core.domain.role.Role;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import br.edu.ifpb.hopin_daw2.core.domain.user.exceptions.PermissionDeniedException;
import br.edu.ifpb.hopin_daw2.core.domain.user.util.UserValidations;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.UserRepository;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository repository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CabService cabService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DriverResponseDTO createDriver(CreateDriverRequestDTO requestDTO) {
        DriverValidations.validateName(requestDTO.name());
        UserValidations.validateEmail(requestDTO.email());
        DriverValidations.validateAge(requestDTO.dateOfBirth());
        UserValidations.verifyEmailAlreadyRegistered(userRepository, requestDTO.email());

        Driver driver = new Driver();
        driver.setName(requestDTO.name());
        driver.setEmail(requestDTO.email());
        driver.setPassword(passwordEncoder.encode(requestDTO.password()));
        driver.setDateOfBirth(requestDTO.dateOfBirth());
        driver.setRole(Role.DRIVER);

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

    public Page<TripResponseDTO> getTripsHistory(int page, int size) {
        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Driver> driver = repository.findByEmail(loggedUser);

        if(driver.isEmpty()) {
            throw new DriverNotFoundException();
        }

        if(!driver.get().getEmail().equals(loggedUser)){
            throw new PermissionDeniedException();
        }

        Page<Trip> trips = tripRepository.findAllByDriverId(driver.get().getId(), PageRequest.of(page, size));

        return trips.map(TripMapper::toDTO);
    }

    public DriverResponseDTO editDriver(EditDriverRequestDTO requestDTO) {
        DriverValidations.validateName(requestDTO.name());
        DriverValidations.validateAge(requestDTO.dateOfBirth());
        UserValidations.validateEmail(requestDTO.email());

        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Driver> driver = repository.findByEmail(loggedUser);

        if(driver.isEmpty()) {
            throw new DriverNotFoundException();
        }

        if(!driver.get().getEmail().equals(loggedUser)){
            throw new PermissionDeniedException();
        }

        Driver driverToEdit = driver.get();

        Cab cab = cabService.editCab(requestDTO);

        driverToEdit.setName(requestDTO.name());
        driverToEdit.setEmail(loggedUser);
        driverToEdit.setDateOfBirth(requestDTO.dateOfBirth());
        driverToEdit.setCab(cab);

        repository.save(driverToEdit);

        return new DriverResponseDTO(
                driverToEdit.getId(),
                driverToEdit.getName(),
                driverToEdit.getEmail(),
                driverToEdit.getDateOfBirth(),
                driverToEdit.getCab(),
                driverToEdit.getCreatedAt()
        );
    }

    public CabResponseDTO getDriverCab(UUID driverId) {
        Optional<Cab> cab = repository.findCabByDriverId(driverId);

        if(cab.isEmpty()) {
            throw new CabNotFoundException();
        }

        return new CabResponseDTO(
                cab.get().getId(),
                cab.get().getModel(),
                cab.get().getColor(),
                cab.get().getPlateNum()
        );
    }

    public boolean deleteDriver(UUID id) {
        Optional<Driver> driverFound = repository.findById(id);

        if(driverFound.isEmpty()) {
            throw new DriverNotFoundException();
        }

        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!driverFound.get().getEmail().equals(loggedUser)){
            throw new PermissionDeniedException();
        }

        repository.delete(driverFound.get());
        return true;
    }
}
