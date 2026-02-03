package com.chubb.abbassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationDto {

    @NotBlank(message = "Language code is required")
    @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
    private String languageCode;

    @NotBlank(message = "Short description is required")
    @Size(max = 255, message = "Short description cannot exceed 255 characters")
    private String shortDescription;

    private String longDescription;
}
