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

    @Operation(summary = "Editar uma viagem",
            description = "Permite editar as informações de uma viagem existente.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Viagem editada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TripResponseDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Erro na validação dos dados fornecidos.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<TripResponseDTO> editTrip(@Parameter(description = "ID da viagem a ser editada")
                                             UUID tripId,
                                             @Parameter(description = "Novos dados da viagem")
                                             TripRequestDTO request);

    @Operation(summary = "Editar o status de uma viagem",
            description = "Permite editar o status de uma viagem existente.",
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
    ResponseEntity<TripResponseDTO> editTripStatus(@Parameter(description = "ID da viagem")
                                                   UUID tripId,
                                                   @Parameter(description = "Novo status da viagem")
                                                   Status status);

    @Operation(summary = "Deletar uma viagem",
            description = "Permite deletar uma viagem existente.",
            tags = { "trip" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Viagem deletada com sucesso."),
            @ApiResponse(responseCode = "404",
                    description = "Viagem não encontrada.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Void> deleteTrip(@Parameter(description = "ID da viagem a ser deletada")
                                    UUID tripId);
}

