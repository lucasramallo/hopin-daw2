package br.edu.ifpb.hopin_daw2.api.controllers.apiDoc;

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
}
