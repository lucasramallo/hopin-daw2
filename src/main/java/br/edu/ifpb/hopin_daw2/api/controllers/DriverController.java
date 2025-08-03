package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.controllers.apiDoc.DriverControllerApi;
import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/driver")
public class DriverController implements DriverControllerApi {
    @Autowired
    private DriverService service;

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable UUID driverId) {
        DriverResponseDTO reponse = service.getDriverById(driverId);

        return ResponseEntity.status(HttpStatus.OK).body(reponse);
    }

    @GetMapping("/getData/{driverEmail}")
    public ResponseEntity<Driver> getDriverByEmail(@PathVariable String driverEmail) {
        Driver reponse = service.getDriverByEmail(driverEmail);

        return ResponseEntity.status(HttpStatus.OK).body(reponse);
    }

    @GetMapping("/getTripsHistory")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Page<TripResponseDTO>> getTripsHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TripResponseDTO> trips = service.getTripsHistory(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(trips);
    }

    @PreAuthorize("hasAnyRole('DRIVER')")
    @GetMapping("/getTripsRequests/{driverd}")
    public ResponseEntity<Page<TripResponseDTO>> getTripsRequests(@PathVariable UUID driverd) {
        int page = 0;
        int size = 10;
        Page<TripResponseDTO> response = service.getTripsRequests(driverd, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cab/{driverId}")
    public ResponseEntity<CabResponseDTO> getDriverCab(@PathVariable UUID driverId) {
        CabResponseDTO cab = service.getDriverCab(driverId);

        return ResponseEntity.status(HttpStatus.OK).body(cab);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<DriverResponseDTO> editDriver(@RequestBody @Valid EditDriverRequestDTO request) {
        DriverResponseDTO reponse = service.editDriver(request);

        return ResponseEntity.status(HttpStatus.OK).body(reponse);
    }

    @DeleteMapping("/{driverId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<DriverResponseDTO> deleteDriver(@PathVariable UUID driverId) {
        service.deleteDriver(driverId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAll() {
        List<Driver> drivers = service.getAll();

        return ResponseEntity.ok(drivers);
    }
}
