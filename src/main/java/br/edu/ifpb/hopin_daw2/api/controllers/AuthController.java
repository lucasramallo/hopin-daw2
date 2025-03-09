package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtUtil;
import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private DriverService driverService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtils;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User userDetails = (User) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());

        return new LoginResponseDTO("Autenticação realizada com sucesso!", jwtToken);
    }


    @PostMapping("/driver/register")
    public ResponseEntity<DriverResponseDTO> createDriver(@RequestBody @Valid CreateDriverRequestDTO request) {
        DriverResponseDTO reponse = driverService.createDriver(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PostMapping("/customer/register")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerRequestDTO request) {
        CustomerResponseDTO response = customerService.createCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}