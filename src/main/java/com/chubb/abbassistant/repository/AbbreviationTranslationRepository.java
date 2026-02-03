package com.chubb.abbassistant.repository;

import com.chubb.abbassistant.entity.AbbreviationTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbbreviationTranslationRepository extends JpaRepository<AbbreviationTranslation, Long> {

    Optional<AbbreviationTranslation> findByAbbreviationIdAndLanguageCode(Long abbreviationId, String languageCode);

    boolean existsByAbbreviationIdAndLanguageCode(Long abbreviationId, String languageCode);

    void deleteByAbbreviationIdAndLanguageCode(Long abbreviationId, String languageCode);
}
