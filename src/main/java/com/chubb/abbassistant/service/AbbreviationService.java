package com.chubb.abbassistant.service;

import com.chubb.abbassistant.dto.AbbreviationRequest;
import com.chubb.abbassistant.dto.AbbreviationResponse;
import com.chubb.abbassistant.dto.TranslationDto;
import com.chubb.abbassistant.entity.Abbreviation;
import com.chubb.abbassistant.entity.AbbreviationTranslation;
import com.chubb.abbassistant.exception.AbbreviationNotFoundException;
import com.chubb.abbassistant.exception.DuplicateAbbreviationException;
import com.chubb.abbassistant.exception.TranslationNotFoundException;
import com.chubb.abbassistant.repository.AbbreviationRepository;
import com.chubb.abbassistant.repository.AbbreviationTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AbbreviationService {

    private final AbbreviationRepository abbreviationRepository;
    private final AbbreviationTranslationRepository translationRepository;

    @Transactional(readOnly = true)
    public List<AbbreviationResponse> findAll() {
        return abbreviationRepository.findAllWithTranslations().stream()
                .map(AbbreviationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AbbreviationResponse findById(Long id) {
        Abbreviation abbreviation = abbreviationRepository.findById(id)
                .orElseThrow(() -> new AbbreviationNotFoundException(id));
        return AbbreviationResponse.fromEntity(abbreviation);
    }

    @Transactional(readOnly = true)
    public AbbreviationResponse findByCode(String code, String languageCode) {
        Abbreviation abbreviation = abbreviationRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> AbbreviationNotFoundException.byCode(code));

        if (languageCode != null && !languageCode.isBlank()) {
            return AbbreviationResponse.fromEntity(abbreviation, languageCode);
        }
        return AbbreviationResponse.fromEntity(abbreviation);
    }

    @Transactional(readOnly = true)
    public List<AbbreviationResponse> search(String query, String languageCode) {
        List<Abbreviation> results;

        if (languageCode != null && !languageCode.isBlank()) {
            results = abbreviationRepository.searchByLanguage(query, languageCode);
        } else {
            results = abbreviationRepository.search(query);
        }

        return results.stream()
                .map(abbr -> languageCode != null && !languageCode.isBlank()
                        ? AbbreviationResponse.fromEntity(abbr, languageCode)
                        : AbbreviationResponse.fromEntity(abbr))
                .collect(Collectors.toList());
    }

    public AbbreviationResponse create(AbbreviationRequest request) {
        if (abbreviationRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new DuplicateAbbreviationException(request.getCode());
        }

        Abbreviation abbreviation = Abbreviation.builder()
                .code(request.getCode().toUpperCase())
                .build();

        if (request.getTranslations() != null) {
            for (TranslationDto dto : request.getTranslations()) {
                AbbreviationTranslation translation = AbbreviationTranslation.builder()
                        .languageCode(dto.getLanguageCode().toLowerCase())
                        .shortDescription(dto.getShortDescription())
                        .longDescription(dto.getLongDescription())
                        .build();
                abbreviation.addTranslation(translation);
            }
        }

        Abbreviation saved = abbreviationRepository.save(abbreviation);
        return AbbreviationResponse.fromEntity(saved);
    }

    public AbbreviationResponse update(Long id, AbbreviationRequest request) {
        Abbreviation abbreviation = abbreviationRepository.findById(id)
                .orElseThrow(() -> new AbbreviationNotFoundException(id));

        // Check if new code conflicts with existing abbreviation
        if (!abbreviation.getCode().equalsIgnoreCase(request.getCode())
                && abbreviationRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new DuplicateAbbreviationException(request.getCode());
        }

        abbreviation.setCode(request.getCode().toUpperCase());

        // Update translations
        abbreviation.getTranslations().clear();
        if (request.getTranslations() != null) {
            for (TranslationDto dto : request.getTranslations()) {
                AbbreviationTranslation translation = AbbreviationTranslation.builder()
                        .languageCode(dto.getLanguageCode().toLowerCase())
                        .shortDescription(dto.getShortDescription())
                        .longDescription(dto.getLongDescription())
                        .build();
                abbreviation.addTranslation(translation);
            }
        }

        Abbreviation saved = abbreviationRepository.save(abbreviation);
        return AbbreviationResponse.fromEntity(saved);
    }

    public void delete(Long id) {
        if (!abbreviationRepository.existsById(id)) {
            throw new AbbreviationNotFoundException(id);
        }
        abbreviationRepository.deleteById(id);
    }

    public AbbreviationResponse addTranslation(Long abbreviationId, TranslationDto translationDto) {
        Abbreviation abbreviation = abbreviationRepository.findById(abbreviationId)
                .orElseThrow(() -> new AbbreviationNotFoundException(abbreviationId));

        // Check if translation for this language already exists
        boolean exists = abbreviation.getTranslations().stream()
                .anyMatch(t -> t.getLanguageCode().equalsIgnoreCase(translationDto.getLanguageCode()));

        if (exists) {
            // Update existing translation
            abbreviation.getTranslations().stream()
                    .filter(t -> t.getLanguageCode().equalsIgnoreCase(translationDto.getLanguageCode()))
                    .findFirst()
                    .ifPresent(t -> {
                        t.setShortDescription(translationDto.getShortDescription());
                        t.setLongDescription(translationDto.getLongDescription());
                    });
        } else {
            // Add new translation
            AbbreviationTranslation translation = AbbreviationTranslation.builder()
                    .languageCode(translationDto.getLanguageCode().toLowerCase())
                    .shortDescription(translationDto.getShortDescription())
                    .longDescription(translationDto.getLongDescription())
                    .build();
            abbreviation.addTranslation(translation);
        }

        Abbreviation saved = abbreviationRepository.save(abbreviation);
        return AbbreviationResponse.fromEntity(saved);
    }

    public AbbreviationResponse updateTranslation(Long abbreviationId, String languageCode, TranslationDto translationDto) {
        Abbreviation abbreviation = abbreviationRepository.findById(abbreviationId)
                .orElseThrow(() -> new AbbreviationNotFoundException(abbreviationId));

        AbbreviationTranslation translation = abbreviation.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(languageCode))
                .findFirst()
                .orElseThrow(() -> new TranslationNotFoundException(abbreviationId, languageCode));

        translation.setLanguageCode(translationDto.getLanguageCode().toLowerCase());
        translation.setShortDescription(translationDto.getShortDescription());
        translation.setLongDescription(translationDto.getLongDescription());

        Abbreviation saved = abbreviationRepository.save(abbreviation);
        return AbbreviationResponse.fromEntity(saved);
    }

    public void deleteTranslation(Long abbreviationId, String languageCode) {
        Abbreviation abbreviation = abbreviationRepository.findById(abbreviationId)
                .orElseThrow(() -> new AbbreviationNotFoundException(abbreviationId));

        AbbreviationTranslation translation = abbreviation.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(languageCode))
                .findFirst()
                .orElseThrow(() -> new TranslationNotFoundException(abbreviationId, languageCode));

        abbreviation.removeTranslation(translation);
        abbreviationRepository.save(abbreviation);
    }
}
