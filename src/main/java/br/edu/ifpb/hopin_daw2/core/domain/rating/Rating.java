package br.edu.ifpb.hopin_daw2.core.domain.rating;

import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity(name = "ratings")
@Table(name = "ratings")
@Data
public class Rating {
    @Id
    @Column
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "trip_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trip trip;

    @Column(nullable = false)
    private int rating;

    @Column(length = 255)
    private String feedback;
}
