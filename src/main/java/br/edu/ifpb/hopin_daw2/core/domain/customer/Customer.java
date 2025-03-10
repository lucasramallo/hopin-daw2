package br.edu.ifpb.hopin_daw2.core.domain.customer;

import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
public class Customer extends User {
    @Column(name = "credit_card_number")
    private String creditCardNumber;

    @Column(name = "credit_card_expiry")
    private String creditCardExpiry;

    @Column(name = "credit_card_cvv")
    private String creditCardCVV;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();
}