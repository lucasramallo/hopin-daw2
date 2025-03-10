package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "driver", description = "API para gerenciar motoristas")
public interface DriverControllerApi {

    @Operation(summary = "Obter dados de um motorista pelo ID",
            description = "Recupera as informações de um motorista a partir do seu identificador único (UUID).",
            tags = { "driver" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Motorista encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Driver not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<DriverResponseDTO> getDriverById(@Parameter(description = "ID do motorista")
                                                    UUID driverId);

    @Operation(summary = "Obter histórico de viagens de um motorista",
            description = "Recupera o histórico de viagens de um motorista com paginação.",
            tags = { "driver" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Histórico de viagens encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Driver not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<List<TripResponseDTO>> getTripsHistory(@Parameter(description = "ID do motorista")
                                                          UUID driverId,
                                                          @Parameter(description = "Número da página")
                                                          int page,
                                                          @Parameter(description = "Número de itens por página")
                                                          int size);

    @Operation(summary = "Obter informações do carro de um motorista",
            description = "Recupera os dados do carro de um motorista, identificando o veículo associado ao motorista.",
            tags = { "driver" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Informações do carro encontrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CabResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Driver not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<CabResponseDTO> getDriverCab(@Parameter(description = "ID do motorista")
                                                UUID driverId);

    @Operation(summary = "Editar dados de um motorista",
            description = "Altera as informações de um motorista.",
            tags = { "driver" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Motorista atualizado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Erro na validação dos dados fornecidos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                    description = "Driver not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<DriverResponseDTO> editDriver(@Parameter(description = "ID do motorista")
                                                 UUID driverId,
                                                 @Parameter(description = "Dados atualizados do motorista")
                                                 EditDriverRequestDTO request);

    @Operation(summary = "Excluir um motorista",
            description = "Deleta um motorista com base no seu ID.",
            tags = { "driver" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Motorista deletado com sucesso."),
            @ApiResponse(responseCode = "404",
                    description = "Driver not found!",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<DriverResponseDTO> deleteDriver(@Parameter(description = "ID do motorista")
                                      UUID driverId);
}
