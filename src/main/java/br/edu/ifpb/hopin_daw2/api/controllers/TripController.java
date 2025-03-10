package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.controllers.apiDoc.TripControllerApi;
import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import br.edu.ifpb.hopin_daw2.core.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO dto) {
        TripResponseDTO response = service.createTrip(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable UUID tripId) {
        TripResponseDTO response = service.getTripById(tripId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //FAZ SENTIDO TER?
    @PutMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> editTrip(@PathVariable UUID tripId, @RequestBody TripRequestDTO request) {
        TripResponseDTO response = service.editTrip(tripId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{tripId}/editStatus")
    public ResponseEntity<TripResponseDTO> editTripStatus(@PathVariable UUID tripId, @RequestBody Status status) {
        TripResponseDTO response = service.editTripStatus(tripId, status);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //FAZ SENTIDO TER?
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable UUID tripId) {
        service.deleteTrip(tripId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
