package br.edu.ifpb.hopin_daw2.core.domain.customer.util;

import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.EmailAlreadyRegisteredException;
import br.edu.ifpb.hopin_daw2.data.jpa.CustomerRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidations {
    public static void verifyEmailAlreadyRegistered(CustomerRepository repository, String email) {
        Optional<Customer> existingCustomer = repository.findByEmail(email);
        if(existingCustomer.isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public static void verifyEmailAlreadyRegistered(CustomerRepository repository, Customer customer, String email) {
        Optional<Customer> existingCustomer = repository.findByEmail(email);

        if(existingCustomer.isPresent()) {
            String existingCustomerId = existingCustomer.get().getId().toString();
            String customerId = customer.getId().toString();

            if(!Objects.equals(existingCustomerId, customerId)) {
                throw new EmailAlreadyRegisteredException();
            }
        }
    }

    public static void validateName(String name) {
        Pattern pattern = Pattern.compile("^[A-ZÀ-ÿ][A-Za-zÀ-ÿ ]{5,50}$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new RuntimeException("Invalid name!");
        }
    }

    public static void validateEmial(String name) {
        Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new RuntimeException("Invalid email!");
        }
    }
}
