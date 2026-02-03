package com.chubb.abbassistant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "abbreviations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abbreviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @OneToMany(mappedBy = "abbreviation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<AbbreviationTranslation> translations = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addTranslation(AbbreviationTranslation translation) {
        translations.add(translation);
        translation.setAbbreviation(this);
    }

    public void removeTranslation(AbbreviationTranslation translation) {
        translations.remove(translation);
        translation.setAbbreviation(null);
    }
}
