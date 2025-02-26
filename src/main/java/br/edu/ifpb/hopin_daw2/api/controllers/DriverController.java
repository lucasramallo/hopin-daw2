package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.DriverResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.EditDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService service;

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable UUID driverId) {
        DriverResponseDTO reponse = service.getDriverById(driverId);

        return ResponseEntity.status(HttpStatus.OK).body(reponse);
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponseDTO> editDriver(@PathVariable UUID driverId, @RequestBody EditDriverRequestDTO request) {
        DriverResponseDTO reponse = service.editDriver(driverId, request);

        return ResponseEntity.status(HttpStatus.OK).body(reponse);
    }
}
