package com.chubb.abbassistant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbbreviationRequest {

    @NotBlank(message = "Abbreviation code is required")
    @Size(min = 1, max = 50, message = "Code must be between 1 and 50 characters")
    private String code;

    @Valid
    @Builder.Default
    private List<TranslationDto> translations = new ArrayList<>();
}
