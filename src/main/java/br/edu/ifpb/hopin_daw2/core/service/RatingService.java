package br.edu.ifpb.hopin_daw2.core.service;

import br.edu.ifpb.hopin_daw2.api.dto.EditRatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;
import br.edu.ifpb.hopin_daw2.core.domain.rating.exceptions.RatingNotFoundExcertion;
import br.edu.ifpb.hopin_daw2.core.domain.rating.util.RatingMapper;
import br.edu.ifpb.hopin_daw2.core.domain.rating.exceptions.RatingNotFound;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.trips.exceprions.TripNotFoundException;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.DriverRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.RatingRepository;
import br.edu.ifpb.hopin_daw2.data.jpa.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TripRepository tripRepository;

    public RatingResponseDTO save(RatingRequestDTO requestDTO) {

        Optional<Driver> driverFound = driverRepository.findById(requestDTO.driverId());
        if(driverFound.isEmpty()) {
            throw new DriverNotFoundException();
        }

        Optional<Customer> customerFound = customerRepository.findById(requestDTO.customerId());
        if(customerFound.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<Trip> tripFound = tripRepository.findById(requestDTO.tripId());
        if(tripFound.isEmpty()) {
            throw new TripNotFoundException();
        }

        Rating rating = new Rating();
        rating.setId(UUID.randomUUID());
        rating.setDriver(driverFound.get());
        rating.setCustomer(customerFound.get());
        rating.setTrip(tripFound.get());
        rating.setRating(requestDTO.rating());
        rating.setFeedback(requestDTO.feedback());

        ratingRepository.save(rating);

        return new RatingResponseDTO(
                rating.getId(),
                rating.getCustomer(),
                rating.getDriver(),
                rating.getTrip(),
                rating.getRating(),
                rating.getFeedback()
        );
    }

    public RatingResponseDTO update(UUID ratingId, EditRatingRequestDTO requestDTO) {
        Optional<Rating> rating = ratingRepository.findById(ratingId);

        if(rating.isEmpty()) {
            throw new RatingNotFoundExcertion("Rating not found!");
        }

        rating.get().setRating(requestDTO.rating());
        rating.get().setFeedback(requestDTO.feedback());

        ratingRepository.save(rating.get());

        return RatingMapper.toDTO(rating.get());
    };

    public void delete(UUID tripId) {
        Optional<Rating> rating = ratingRepository.findByTripId(tripId);

        rating.ifPresent(value -> ratingRepository.delete(value));
    }
}
