package br.edu.ifpb.hopin_daw2.core.domain.driver;

import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "drivers")
@Table(name = "drivers")
@Data
public class Driver {
    @Id
    @Column
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "cab_id")
    private Cab cab;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}