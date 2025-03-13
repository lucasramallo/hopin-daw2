package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

@Tag(name = "customer", description = "API para gerenciar clientes")
public interface CustomerControllerApi {

    @Operation(summary = "Obter dados de um cliente pelo ID",
            description = "Recupera as informações de um cliente a partir do seu identificador único (UUID).",
            tags = { "customer" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<CustomerResponseDTO> getCustomerById(@Parameter(description = "ID do cliente")
                                                        UUID customerId);

    @Operation(summary = "Obter dados de um cliente pelo email",
            description = "Recupera as informações de um cliente a partir de seu e-mail.",
            tags = { "customer" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@Parameter(description = "E-mail do cliente")
                                                           String email);

    @Operation(summary = "Obter histórico de viagens de um cliente",
            description = "Recupera o histórico de viagens de um cliente com paginação.",
            tags = { "customer" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Histórico de viagens encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Page<TripResponseDTO>> getTripsHistory(@Parameter(description = "Número da página")
                                                          int page,
                                                          @Parameter(description = "Número de itens por página")
                                                          int size);

    @Operation(summary = "Editar dados de um cliente",
            description = "Altera as informações de um cliente.",
            tags = { "customer" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente atualizado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<CustomerResponseDTO> editCustomer(@Parameter(description = "Dados atualizados do cliente")
                                                     CustomerRequestDTO request);

    @Operation(summary = "Excluir um cliente",
            description = "Deleta um cliente com base no seu ID.",
            tags = { "customer" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Cliente deletado com sucesso."),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found!",
                    content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<CustomerResponseDTO> deleteCustomer(@Parameter(description = "ID do cliente")
                                        UUID customerId);
}
