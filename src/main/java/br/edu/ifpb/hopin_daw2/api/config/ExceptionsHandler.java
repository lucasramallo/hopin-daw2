package br.edu.ifpb.hopin_daw2.api.config;

import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.CabNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.payments.exceptions.NoCreditCardInfo;
import br.edu.ifpb.hopin_daw2.core.domain.rating.exceptions.RatingNotFoundExcertion;
import br.edu.ifpb.hopin_daw2.core.domain.user.exceptions.EmailAlreadyRegisteredException;
import br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions.InvalidPlateException;
import br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions.CustomerNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.DriverNotFoundException;
import br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions.UnderageDriverException;
import br.edu.ifpb.hopin_daw2.api.globalExceptions.*;
import br.edu.ifpb.hopin_daw2.core.domain.trips.exceprions.TripNotFoundException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    enum ErrorType {
        UNEXPECTED_ERROR,
        VALIDATION_ERROR,
        EMAIL_ALREADY_REGISTERED,
        CUSTOMER_NOT_FOUND,
        DRIVER_NOT_FOUND,
        CAB_NOT_FOUND,
        TRIP_NOT_FOUND,
        RATING_NOT_FOUND,
        INVALID_CREDENTIALS,
        INVALID_USER_NAME,
        INVALID_USER_EMAIL,
        AUTHENTICATION_ERROR,
        REQUIRED_FIELDS_NOT_FILLED,
        INVALID_TOKEN,
        UNDERAGE_DRIVER_ERROR,
        INVALID_CAB_PLATE,
        ACCES_DENIED,
        NO_CREDIT_CARD_INFO;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        return buildProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.UNEXPECTED_ERROR);
    }

    //User

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ProblemDetail handleEmailAlreadyRegistered(EmailAlreadyRegisteredException e) {
        return buildProblemDetail(e, HttpStatus.CONFLICT, ErrorType.EMAIL_ALREADY_REGISTERED);
    }

    @ExceptionHandler(InvalidUserNameException.class)
    public ProblemDetail handleInvalidUserName(InvalidUserNameException e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.INVALID_USER_NAME);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ProblemDetail handleInvalidEmail(InvalidEmailException e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.INVALID_USER_EMAIL);
    }

    //Customer

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFound(CustomerNotFoundException e) {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, ErrorType.CUSTOMER_NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException e) {
        return buildProblemDetail(e, HttpStatus.UNAUTHORIZED, ErrorType.INVALID_CREDENTIALS);
    }

    //Global

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAuthenticationException(AccessDeniedException e) {
        return buildProblemDetail(e, HttpStatus.FORBIDDEN, ErrorType.ACCES_DENIED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        return buildProblemDetail(e, HttpStatus.UNAUTHORIZED, ErrorType.AUTHENTICATION_ERROR);
    }

    @ExceptionHandler(RequiredFieldsException.class)
    public ProblemDetail handleRequiredFields(RequiredFieldsException e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.REQUIRED_FIELDS_NOT_FILLED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(InvalidTokenException e) {
        return buildProblemDetail(e, HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN);
    }

    // Driver

    @ExceptionHandler(UnderageDriverException.class)
    public ProblemDetail handleUnderageDriver(UnderageDriverException e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.UNDERAGE_DRIVER_ERROR);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ProblemDetail handleDriverNotFound(DriverNotFoundException e) {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, ErrorType.DRIVER_NOT_FOUND);
    }

    //Cab

    @ExceptionHandler(InvalidPlateException.class)
    public ProblemDetail handleInvalidPlate(InvalidPlateException e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.INVALID_CAB_PLATE);
    }

    @ExceptionHandler(CabNotFoundException.class)
    public ProblemDetail handleCabNotFound(CabNotFoundException e) {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, ErrorType.CAB_NOT_FOUND);
    }

    // Trip

    @ExceptionHandler(TripNotFoundException.class)
    public ProblemDetail handleTripNotFound(TripNotFoundException e) {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, ErrorType.TRIP_NOT_FOUND);
    }

    // Rating
    @ExceptionHandler(RatingNotFoundExcertion.class)
    public ProblemDetail handleTripNotFound(RatingNotFoundExcertion e) {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, ErrorType.RATING_NOT_FOUND);
    }

    //Payment
    @ExceptionHandler(NoCreditCardInfo.class)
    public ProblemDetail handleTripNotFound(NoCreditCardInfo e) {
        return buildProblemDetail(e, HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.NO_CREDIT_CARD_INFO);
    }

    // Validations

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.ofNullable(handleMethodArgumentNotValidException(ex));
    }

    private ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = buildProblemDetail(ex, HttpStatus.BAD_REQUEST, ErrorType.VALIDATION_ERROR);
        problemDetail.setProperty("erros", errors);

        return problemDetail;
    }

    private ProblemDetail buildProblemDetail(Exception ex, HttpStatus status, ErrorType type) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(type.name()));
        problemDetail.setProperty("trace", stackTraceToString(ex));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    private String stackTraceToString(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));

        return errors.toString();
    }
}
