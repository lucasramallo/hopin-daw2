package br.edu.ifpb.hopin_daw2.core.domain.customer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidations {
    public static void validateName(String name) {
        Pattern pattern = Pattern.compile("^[A-ZÀ-ÿ][A-Za-zÀ-ÿ ]{5,50}$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new RuntimeException("Invalid name!");
        }
    }
}
