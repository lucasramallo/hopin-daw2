package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.controllers.apiDoc.RatingControllerApi;
import br.edu.ifpb.hopin_daw2.api.dto.EditRatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingResponseDTO;
import br.edu.ifpb.hopin_daw2.core.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rating")
public class RatingController implements RatingControllerApi {
    @Autowired
    private RatingService ratingService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<RatingResponseDTO> createRating(@RequestBody @Valid RatingRequestDTO requestDTO) {
        RatingResponseDTO responseDTO = ratingService.save(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{ratingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<RatingResponseDTO> updateRating(@PathVariable UUID ratingId, @RequestBody @Valid EditRatingRequestDTO requestDTO) {
        RatingResponseDTO responseDTO = ratingService.update(ratingId, requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
