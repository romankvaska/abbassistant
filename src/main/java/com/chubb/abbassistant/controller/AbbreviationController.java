package com.chubb.abbassistant.controller;

import com.chubb.abbassistant.dto.AbbreviationRequest;
import com.chubb.abbassistant.dto.AbbreviationResponse;
import com.chubb.abbassistant.dto.TranslationDto;
import com.chubb.abbassistant.service.AbbreviationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abbreviations")
@RequiredArgsConstructor
@Tag(name = "Abbreviations", description = "API for managing abbreviations with multi-language support")
public class AbbreviationController {

    private final AbbreviationService abbreviationService;

    @GetMapping
    @Operation(summary = "Get all abbreviations", description = "Returns a list of all abbreviations with their translations")
    public ResponseEntity<List<AbbreviationResponse>> getAll() {
        return ResponseEntity.ok(abbreviationService.findAll());
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get abbreviation by code", description = "Returns an abbreviation by its code, optionally filtered by language")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Abbreviation found"),
            @ApiResponse(responseCode = "404", description = "Abbreviation not found")
    })
    public ResponseEntity<AbbreviationResponse> getByCode(
            @Parameter(description = "Abbreviation code (e.g., API, HTTP)")
            @PathVariable String code,
            @Parameter(description = "Language code (e.g., en, de, fr)")
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(abbreviationService.findByCode(code, lang));
    }

    @GetMapping("/search")
    @Operation(summary = "Search abbreviations", description = "Search abbreviations by code or description")
    public ResponseEntity<List<AbbreviationResponse>> search(
            @Parameter(description = "Search query")
            @RequestParam String q,
            @Parameter(description = "Language code to filter results")
            @RequestParam(required = false) String lang) {
        return ResponseEntity.ok(abbreviationService.search(q, lang));
    }

    @PostMapping
    @Operation(summary = "Create a new abbreviation", description = "Creates a new abbreviation with optional translations")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Abbreviation created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Abbreviation already exists")
    })
    public ResponseEntity<AbbreviationResponse> create(@Valid @RequestBody AbbreviationRequest request) {
        AbbreviationResponse response = abbreviationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an abbreviation", description = "Updates an existing abbreviation and its translations")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Abbreviation updated"),
            @ApiResponse(responseCode = "404", description = "Abbreviation not found"),
            @ApiResponse(responseCode = "409", description = "Code already in use")
    })
    public ResponseEntity<AbbreviationResponse> update(
            @Parameter(description = "Abbreviation ID")
            @PathVariable Long id,
            @Valid @RequestBody AbbreviationRequest request) {
        return ResponseEntity.ok(abbreviationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an abbreviation", description = "Deletes an abbreviation and all its translations")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Abbreviation deleted"),
            @ApiResponse(responseCode = "404", description = "Abbreviation not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Abbreviation ID")
            @PathVariable Long id) {
        abbreviationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/translations")
    @Operation(summary = "Add a translation", description = "Adds a new translation to an existing abbreviation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Translation added"),
            @ApiResponse(responseCode = "404", description = "Abbreviation not found")
    })
    public ResponseEntity<AbbreviationResponse> addTranslation(
            @Parameter(description = "Abbreviation ID")
            @PathVariable Long id,
            @Valid @RequestBody TranslationDto translation) {
        AbbreviationResponse response = abbreviationService.addTranslation(id, translation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/translations/{lang}")
    @Operation(summary = "Update a translation", description = "Updates an existing translation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Translation updated"),
            @ApiResponse(responseCode = "404", description = "Abbreviation or translation not found")
    })
    public ResponseEntity<AbbreviationResponse> updateTranslation(
            @Parameter(description = "Abbreviation ID")
            @PathVariable Long id,
            @Parameter(description = "Language code of the translation to update")
            @PathVariable String lang,
            @Valid @RequestBody TranslationDto translation) {
        return ResponseEntity.ok(abbreviationService.updateTranslation(id, lang, translation));
    }

    @DeleteMapping("/{id}/translations/{lang}")
    @Operation(summary = "Delete a translation", description = "Deletes a translation from an abbreviation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Translation deleted"),
            @ApiResponse(responseCode = "404", description = "Abbreviation or translation not found")
    })
    public ResponseEntity<Void> deleteTranslation(
            @Parameter(description = "Abbreviation ID")
            @PathVariable Long id,
            @Parameter(description = "Language code of the translation to delete")
            @PathVariable String lang) {
        abbreviationService.deleteTranslation(id, lang);
        return ResponseEntity.noContent().build();
    }
}
