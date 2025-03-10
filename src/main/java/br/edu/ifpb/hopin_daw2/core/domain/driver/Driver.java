package br.edu.ifpb.hopin_daw2.core.domain.driver;

import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "drivers")
@Table(name = "drivers")
@Data
public class Driver extends User {
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cab_id")
    private Cab cab;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
}