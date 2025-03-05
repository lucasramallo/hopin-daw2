package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.TripRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Payment;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.trips.exceprions.TripNotFoundException;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import br.edu.ifpb.hopin_daw2.mappers.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TripMapper tripMapper;

    public TripResponseDTO createTrip(TripRequestDTO dto) {
        Optional<Customer> customer = customerRepository.findById(dto.customerId());
        Optional<Driver> driver = driverRepository.findById(dto.driverId());

        if(customer.isEmpty()){
            throw new CustomerNotFoundException();
        }

        if(driver.isEmpty()){
            throw new DriverNotFoundException();
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
        trip.setStatus(dto.status());
        trip.setOrigin(dto.origin());
        trip.setDestination(dto.destination());
        trip.setCreatedAt(LocalDateTime.now());

        tripRepository.save(trip);

        return tripMapper.toDTO(trip);
    }

    public TripResponseDTO getTripById(UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        return tripMapper.toDTO(trip.get());
    }

    public TripResponseDTO editTrip(UUID tripId, TripRequestDTO dto) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        Optional<Customer> customer = customerRepository.findById(dto.customerId());
        Optional<Driver> driver = driverRepository.findById(dto.driverId());

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        if(customer.isEmpty()){
            throw new CustomerNotFoundException();
        }

        if(driver.isEmpty()){
            throw new DriverNotFoundException();
        }

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setMethod(dto.paymentMethod());
        payment.setAmount(dto.paymentAmount());
        payment.setCreatedAt(LocalDateTime.now());

        Trip tripToEdit = trip.get();
        tripToEdit.setCustomer(customer.get());
        tripToEdit.setDriver(driver.get());
        tripToEdit.setPayment(payment);
        tripToEdit.setStatus(dto.status());
        tripToEdit.setOrigin(dto.origin());
        tripToEdit.setDestination(dto.destination());

        tripRepository.save(trip.get());

        return tripMapper.toDTO(trip.get());
    }

    public TripResponseDTO editTripStatus(UUID tripId, Status status) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        trip.get().setStatus(status);

        tripRepository.save(trip.get());

        return tripMapper.toDTO(trip.get());
    }

    public void deleteTrip(UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            throw new TripNotFoundException();
        }

        tripRepository.delete(trip.get());
    }
}
