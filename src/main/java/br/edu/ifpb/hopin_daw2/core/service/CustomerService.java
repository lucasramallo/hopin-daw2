package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.customer.util.CustomerValidations;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;
import br.edu.ifpb.hopin_daw2.mappers.CustomerMapper;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        CustomerValidations.validateEmail(dto.email());
        CustomerValidations.verifyEmailAlreadyRegistered(repository, dto.email());
        CustomerValidations.validateName(dto.name());

        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPassword(dto.password());
        customer.setCreatedAt(LocalDateTime.now());

        repository.save(customer);

        return customerMapper.toDTO(customer);
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return customerMapper.toDTO(customer.get());
    }

    public CustomerResponseDTO getCustomerByEmail(String email) {
        Optional<Customer> customer = repository.findByEmail(email);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return customerMapper.toDTO(customer.get());
    }

    public Page<TripResponseDTO> getTripsHistory(UUID customerId, Integer page, Integer size) {
        Page<Trip> trips = repository.getTripsHistory(customerId, PageRequest.of(page, size));

        return trips.map(trip -> tripMapper.toDTO(trip));
    }

    public CustomerResponseDTO editCustomer(UUID id, CustomerRequestDTO dto) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customerFound = customer.get();

        customerFound.setName(dto.name());
        customerFound.setEmail(dto.email());
        customerFound.setPassword(dto.password());

        repository.save(customer.get());

        return customerMapper.toDTO(customerFound);
    }

    public void deleteCustomer(UUID id) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        repository.delete(customer.get());
    }
}
