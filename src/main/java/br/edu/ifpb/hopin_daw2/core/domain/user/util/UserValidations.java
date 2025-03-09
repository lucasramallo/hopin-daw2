package br.edu.ifpb.hopin_daw2.core.domain.user.util;

import br.edu.ifpb.hopin_daw2.api.globalExceptions.InvalidEmailException;
import br.edu.ifpb.hopin_daw2.core.domain.user.exceptions.EmailAlreadyRegisteredException;
import br.edu.ifpb.hopin_daw2.core.domain.user.User;
import br.edu.ifpb.hopin_daw2.data.jpa.UserRepository;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidations {
    public static void verifyEmailAlreadyRegistered(UserRepository repository, String email) {
        Optional<User> existingCustomer = repository.findByEmail(email);
        if(existingCustomer.isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public static void validateEmail(String name) {
        Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new InvalidEmailException("Invalid email: " + name);
        }
    }
}
