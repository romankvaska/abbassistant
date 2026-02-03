package com.chubb.abbassistant.repository;

import com.chubb.abbassistant.entity.Abbreviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbbreviationRepository extends JpaRepository<Abbreviation, Long> {

    Optional<Abbreviation> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    @Query("SELECT DISTINCT a FROM Abbreviation a " +
           "LEFT JOIN FETCH a.translations t " +
           "WHERE LOWER(a.code) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(t.shortDescription) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(t.longDescription) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Abbreviation> search(@Param("query") String query);

    @Query("SELECT DISTINCT a FROM Abbreviation a " +
           "LEFT JOIN FETCH a.translations t " +
           "WHERE (LOWER(a.code) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(t.shortDescription) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(t.longDescription) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND t.languageCode = :lang")
    List<Abbreviation> searchByLanguage(@Param("query") String query, @Param("lang") String lang);

    @Query("SELECT a FROM Abbreviation a LEFT JOIN FETCH a.translations")
    List<Abbreviation> findAllWithTranslations();
}
