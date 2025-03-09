package br.edu.ifpb.hopin_daw2.data.jpa;

import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
