package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

import br.edu.ifpb.hopin_daw2.api.dto.EditRatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "rating", description = "API para gerenciar avaliações de motoristas")
public interface RatingControllerApi {

    @Operation(summary = "Criar uma avaliação para um motorista",
            description = "Permite que um cliente crie uma avaliação para um motorista.",
            tags = { "rating" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Avaliação criada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RatingResponseDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "Acesso proibido, o usuário deve ser um cliente.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<RatingResponseDTO> createRating(@Parameter(description = "Dados da avaliação do cliente")
                                                   RatingRequestDTO requestDTO);

    @Operation(summary = "Editar uma avaliação",
            description = "Permite que um cliente edite uma avaliação existente.",
            tags = { "rating" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Avaliação atualizada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RatingResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Avaliação não encontrada.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                    description = "Erro inesperado.",
                    content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<RatingResponseDTO> updateRating(@Parameter(description = "ID da avaliação a ser editada") UUID ratingId,
                                                   @Parameter(description = "Novos dados para a avaliação") EditRatingRequestDTO requestDTO);
}
