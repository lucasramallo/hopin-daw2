package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService service;

    @PostMapping()
    public void createDriver(@RequestBody CreateDriverRequestDTO request) {
        service.createDriver(request);
    }
}
