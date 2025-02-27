package br.edu.ifpb.hopin_daw2.core.domain.driver.util;

import br.edu.ifpb.hopin_daw2.api.dto.CreateDriverRequestDTO;
import br.edu.ifpb.hopin_daw2.api.globalExceptions.InvalidUserNameException;
import br.edu.ifpb.hopin_daw2.api.globalExceptions.InvalidEmailException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.UnderageDriverException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverValidations {
    public static void validateName(String name) {
        Pattern pattern = Pattern.compile("^[A-ZÀ-ÿ][A-Za-zÀ-ÿ ]{5,50}$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new InvalidUserNameException("Invalid name!");
        }
    }

    public static void validateAge(LocalDate age) {
        int driverAge = Period.between(age, LocalDate.now()).getYears();

        if(driverAge < 18) {
            throw new UnderageDriverException("The user cannot be underage.");
        }
    }

    public static void validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailException("Invalid email format!");
        }
    }
}
