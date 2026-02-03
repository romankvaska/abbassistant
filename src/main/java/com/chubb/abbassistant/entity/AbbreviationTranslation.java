package com.chubb.abbassistant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "abbreviation_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"abbreviation_id", "language_code"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbbreviationTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abbreviation_id", nullable = false)
    private Abbreviation abbreviation;

    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "short_description", nullable = false, length = 255)
    private String shortDescription;

    @Column(name = "long_description", columnDefinition = "TEXT")
    private String longDescription;
}
