package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "auth", description = "API de autenticação e registro de usuários")
public interface AuthControllerApi {
    // http://localhost:8080/api/swagger-ui/index.html#/
    @Operation(summary = "Realizar login de usuário",
            description = "Autentica o usuário e gera um token JWT para autenticação.",
            tags = { "auth" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Autenticação realizada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401",
                    description = "Credenciais inválidas.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
    })
    ResponseEntity<LoginResponseDTO> login(@Parameter(description = "Dados de login do usuário")
                                           LoginRequestDTO request);

    @Operation(summary = "Registrar um motorista",
            description = "Cria um novo motorista a partir dos dados informados.",
            tags = { "auth" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Motorista criado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Erro nos dados fornecidos.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<DriverResponseDTO> createDriver(@Parameter(description = "Dados para criação do motorista")
                                                   CreateDriverRequestDTO request);

    @Operation(summary = "Registrar um cliente",
            description = "Cria um novo cliente a partir dos dados informados.",
            tags = { "auth" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cliente criado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Erro nos dados fornecidos.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<CustomerResponseDTO> createCustomer(@Parameter(description = "Dados para criação do cliente")
                                                       CustomerRequestDTO request);
}
