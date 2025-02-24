package br.edu.ifpb.hopin_daw2.data.jpa;

import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
}
