package br.edu.ifpb.hopin_daw2.core.domain.cab;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cabs")
@Table(name = "cabs")
@Data
public class Cab {
    @Id
    @Column
    private UUID id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(name = "plate_num", nullable = false)
    private String plateNum;

    @PrePersist
    private void init() {
        this.id = UUID.randomUUID();
    }
}