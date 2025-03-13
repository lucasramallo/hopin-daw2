package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.TripRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Method;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Payment;
import br.edu.ifpb.hopin_daw2.core.domain.payments.exceptions.NoCreditCardInfo;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.trips.exceptions.InvalidTripStatusUpdateException;
import br.edu.ifpb.hopin_daw2.core.domain.trips.exceptions.TripNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.user.exceptions.PermissionDeniedException;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DriverRepository driverRepository;

    public TripResponseDTO createTrip(TripRequestDTO dto) {
        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Customer> customer = customerRepository.findByEmail(loggedUser);
        Optional<Driver> driver = driverRepository.findById(dto.driverId());

        if(customer.isEmpty()){
            throw new CustomerNotFoundException();
        }

        if(driver.isEmpty()){
            throw new DriverNotFoundException();
        }

        if(dto.paymentMethod() == Method.CREDIT_CARD && customer.get().getCreditCardNumber() == null) {
            throw new NoCreditCardInfo("Customer have not credit card info");
        }

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setMethod(dto.paymentMethod());
        payment.setAmount(dto.paymentAmount());
        payment.setCreatedAt(LocalDateTime.now());

        Trip trip = new Trip();
        trip.setId(UUID.randomUUID());
        trip.setCustomer(customer.get());
        trip.setDriver(driver.get());
        trip.setPayment(payment);
        trip.setStatus(Status.REQUESTED);
        trip.setOrigin(dto.origin());
        trip.setDestination(dto.destination());
        trip.setCreatedAt(LocalDateTime.now());

        tripRepository.save(trip);

        return TripMapper.toDTO(trip);
    }

    public TripResponseDTO getTripById(UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        return TripMapper.toDTO(trip.get());
    }

    public TripResponseDTO editTripStatus(UUID tripId, Status status) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        String loggedUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if((status == Status.CANCELLED && !trip.get().getCustomer().getEmail().equals(loggedUser) && !trip.get().getDriver().getEmail().equals(loggedUser)) ||
                (status != Status.CANCELLED && !trip.get().getDriver().getEmail().equals(loggedUser))){
            throw new PermissionDeniedException();
        }

        if((status == Status.ACCEPTED && !trip.get().getStatus().equals(Status.REQUESTED)) ||
                (status == Status.CANCELLED && (!trip.get().getStatus().equals(Status.REQUESTED) && !trip.get().getStatus().equals(Status.ACCEPTED))) ||
                (status == Status.STARTED && !trip.get().getStatus().equals(Status.ACCEPTED)) ||
                (status == Status.COMPLETED && !trip.get().getStatus().equals(Status.STARTED))){
            throw new InvalidTripStatusUpdateException(trip.get().getStatus().name(), status.name());
        }

        trip.get().setStatus(status);

        tripRepository.save(trip.get());

        return TripMapper.toDTO(trip.get());
    }

    public void deleteTrip(UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        tripRepository.delete(trip.get());
    }
}
