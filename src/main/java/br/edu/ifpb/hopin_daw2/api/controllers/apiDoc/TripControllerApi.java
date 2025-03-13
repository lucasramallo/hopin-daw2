package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "trip", description = "API para gerenciar viagens")
public interface TripControllerApi {

    @Operation(summary = "Criar uma nova viagem",
            description = "Permite que um cliente crie uma nova viagem.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Viagem criada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Erro na validação dos dados fornecidos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403",
                    description = "Acesso proibido, o usuário deve ser um cliente.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> createTrip(@Parameter(description = "Dados da viagem a ser criada")
                                               TripRequestDTO dto);

    @Operation(summary = "Buscar viagem por ID",
            description = "Permite que um usuário recupere os detalhes de uma viagem pelo seu ID.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Viagem recuperada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> getTripById(@Parameter(description = "ID da viagem a ser recuperada")
                                                UUID tripId);

    @Operation(summary = "Editar o status de uma viagem para ACCEPTED",
            description = "Permite editar o status de uma viagem existente para ACCEPTED.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status da viagem editado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> acceptTrip(@Parameter(description = "ID da viagem")
                                                   UUID tripId);

    @Operation(summary = "Editar o status de uma viagem para CANCELLED",
            description = "Permite editar o status de uma viagem existente para CANCELLED.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status da viagem editado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> cancelTrip(@Parameter(description = "ID da viagem")
                                                   UUID tripId);

    @Operation(summary = "Editar o status de uma viagem para STARTED",
            description = "Permite editar o status de uma viagem existente para STARTED.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status da viagem editado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> startTrip(@Parameter(description = "ID da viagem")
                                                   UUID tripId);

    @Operation(summary = "Editar o status de uma viagem para COMPLETED",
            description = "Permite editar o status de uma viagem existente para COMPLETED.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Status da viagem editado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> completeTrip(@Parameter(description = "ID da viagem")
                                                   UUID tripId);
}

