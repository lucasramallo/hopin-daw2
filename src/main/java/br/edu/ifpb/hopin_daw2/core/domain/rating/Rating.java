package br.edu.ifpb.hopin_daw2.core.domain.rating;

import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import jakarta.persistence.*;

import lombok.Data;

import java.util.UUID;

@Entity(name = "ratings")
@Table(name = "ratings")
@Data
public class Rating {
    @Id
    @Column
    private UUID id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private int rating;

    @Column(length = 255)
    private String feedback;
}
