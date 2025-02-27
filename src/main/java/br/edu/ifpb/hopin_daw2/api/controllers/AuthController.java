package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.DriverResponseDTO;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private DriverService service;

    @PostMapping("/driver/register")
    public ResponseEntity<DriverResponseDTO> createDriver(@RequestBody CreateDriverRequestDTO request) {
        DriverResponseDTO reponse = service.createDriver(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }
}