package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.controllers.apiDoc.CustomerControllerApi;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerControllerApi {
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

    @GetMapping("/getTripsHistory")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Page<TripResponseDTO>> getTripsHistory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<TripResponseDTO> trips = service.getTripsHistory(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(trips);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> editCustomer(@RequestBody @Valid CustomerRequestDTO request) {
        CustomerResponseDTO response = service.editCustomer(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(@PathVariable UUID customerId) {
        service.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}
