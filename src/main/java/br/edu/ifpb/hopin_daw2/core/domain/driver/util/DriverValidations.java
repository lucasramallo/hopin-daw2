package br.edu.ifpb.hopin_daw2.core.domain.driver.util;

import github.lucasramallo.hopin.core.domain.driver.exceptions.UnderageDriverException;
import github.lucasramallo.hopin.core.globalExceptions.InvalidUserNameException;

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
            throw new UnderageDriverException("Underage Driver!");
        }
    }
}
