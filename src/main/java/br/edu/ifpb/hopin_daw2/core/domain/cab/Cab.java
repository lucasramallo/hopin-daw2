package br.edu.ifpb.hopin_daw2.core.domain.cab;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}