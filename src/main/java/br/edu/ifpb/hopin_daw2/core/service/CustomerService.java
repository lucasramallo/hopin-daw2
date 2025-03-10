package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.customer.util.CustomerValidations;
import br.edu.ifpb.hopin_daw2.core.domain.role.Role;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.user.util.UserValidations;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.UserRepository;
import br.edu.ifpb.hopin_daw2.mappers.CustomerMapper;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        UserValidations.validateEmail(dto.email());
        UserValidations.verifyEmailAlreadyRegistered(userRepository, dto.email());
        CustomerValidations.validateName(dto.name());

        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customer.setRole(Role.CUSTOMER);
        customer.setCreditCardNumber(dto.creditCardNumber());
        customer.setCreditCardCVV(dto.creditCardCVV());
        customer.setCreditCardExpiry(dto.creditCardExpiry());

        customerRepository.save(customer);

        return customerMapper.toDTO(customer);
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return customerMapper.toDTO(customer.get());
    }

    public CustomerResponseDTO getCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return customerMapper.toDTO(customer.get());
    }

    public Page<TripResponseDTO> getTripsHistory(UUID customerId, Integer page, Integer size) {
        Page<Trip> trips = tripRepository.findAllByCustomerId(customerId, PageRequest.of(page, size));

        return trips.map(TripMapper::toDTO);
    }

    public CustomerResponseDTO editCustomer(UUID id, CustomerRequestDTO dto) {
        Optional<Customer> customer = customerRepository.findById(id);
        UserValidations.validateEmail(dto.email());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customerFound = customer.get();

        customerFound.setName(dto.name());
        customerFound.setEmail(dto.email());
        customerFound.setPassword(dto.password());
        customerFound.setCreditCardNumber(dto.creditCardNumber());
        customerFound.setCreditCardCVV(dto.creditCardCVV());
        customerFound.setCreditCardExpiry(dto.creditCardExpiry());

        customerRepository.save(customer.get());

        return customerMapper.toDTO(customerFound);
    }

    public void deleteCustomer(UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        customerRepository.delete(customer.get());
    }
}
