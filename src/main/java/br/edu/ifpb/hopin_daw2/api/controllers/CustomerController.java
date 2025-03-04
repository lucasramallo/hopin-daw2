package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable UUID customerId) {
        CustomerResponseDTO response = service.getCustomerById(customerId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@RequestParam String email) {
        CustomerResponseDTO response = service.getCustomerByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{customerId}/getTripsHistory")
    public ResponseEntity<Page<TripResponseDTO>> getTripsHistory(@PathVariable UUID customerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<TripResponseDTO> trips = service.getTripsHistory(customerId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(trips);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> editCustomer(@PathVariable UUID customerId, @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO response = service.editCustomer(customerId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(@PathVariable UUID customerId) {
        service.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}
