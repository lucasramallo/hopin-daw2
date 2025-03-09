package br.edu.ifpb.hopin_daw2.mappers;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponseDTO toDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }

    public Customer toEntity(CustomerResponseDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setCreatedAt(dto.createdAt());

        return customer;
    }
}
