package com.chubb.abbassistant.dto;

import com.chubb.abbassistant.entity.Abbreviation;
import com.chubb.abbassistant.entity.AbbreviationTranslation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbbreviationResponse {

    private Long id;
    private String code;
    private List<TranslationDto> translations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AbbreviationResponse fromEntity(Abbreviation abbreviation) {
        return AbbreviationResponse.builder()
                .id(abbreviation.getId())
                .code(abbreviation.getCode())
                .translations(abbreviation.getTranslations().stream()
                        .map(AbbreviationResponse::toTranslationDto)
                        .collect(Collectors.toList()))
                .createdAt(abbreviation.getCreatedAt())
                .updatedAt(abbreviation.getUpdatedAt())
                .build();
    }

    public static AbbreviationResponse fromEntity(Abbreviation abbreviation, String languageCode) {
        List<TranslationDto> filteredTranslations = abbreviation.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(languageCode))
                .map(AbbreviationResponse::toTranslationDto)
                .collect(Collectors.toList());

        return AbbreviationResponse.builder()
                .id(abbreviation.getId())
                .code(abbreviation.getCode())
                .translations(filteredTranslations)
                .createdAt(abbreviation.getCreatedAt())
                .updatedAt(abbreviation.getUpdatedAt())
                .build();
    }

    private static TranslationDto toTranslationDto(AbbreviationTranslation translation) {
        return TranslationDto.builder()
                .languageCode(translation.getLanguageCode())
                .shortDescription(translation.getShortDescription())
                .longDescription(translation.getLongDescription())
                .build();
    }
}
