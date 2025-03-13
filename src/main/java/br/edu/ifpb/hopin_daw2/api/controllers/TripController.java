package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.controllers.apiDoc.TripControllerApi;
import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.service.TripService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trip")
public class TripController implements TripControllerApi {
    @Autowired
    private TripService service;

    @PostMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody @Valid TripRequestDTO dto) {
        TripResponseDTO response = service.createTrip(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable UUID tripId) {
        TripResponseDTO response = service.getTripById(tripId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('DRIVER')")
    @PatchMapping("/{tripId}/accept")
    public ResponseEntity<TripResponseDTO> acceptTrip(@PathVariable UUID tripId) {
        TripResponseDTO response = service.editTripStatus(tripId, Status.ACCEPTED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{tripId}/cancel")
    public ResponseEntity<TripResponseDTO> cancelTrip(@PathVariable UUID tripId) {
        TripResponseDTO response = service.editTripStatus(tripId, Status.CANCELLED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('DRIVER')")
    @PatchMapping("/{tripId}/start")
    public ResponseEntity<TripResponseDTO> startTrip(@PathVariable UUID tripId) {
        TripResponseDTO response = service.editTripStatus(tripId, Status.STARTED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('DRIVER')")
    @PatchMapping("/{tripId}/complete")
    public ResponseEntity<TripResponseDTO> completeTrip(@PathVariable UUID tripId) {
        TripResponseDTO response = service.editTripStatus(tripId, Status.COMPLETED);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
