package com.chubb.abbassistant.config;

import com.chubb.abbassistant.entity.Abbreviation;
import com.chubb.abbassistant.entity.AbbreviationTranslation;
import com.chubb.abbassistant.repository.AbbreviationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final AbbreviationRepository abbreviationRepository;

    @Override
    public void run(String... args) {
        if (abbreviationRepository.count() == 0) {
            log.info("Loading sample abbreviations...");
            loadSampleData();
            log.info("Sample data loaded successfully!");
        }
    }

    private void loadSampleData() {
        // API
        Abbreviation api = Abbreviation.builder().code("API").build();
        api.addTranslation(AbbreviationTranslation.builder()
                .languageCode("en")
                .shortDescription("Application Programming Interface")
                .longDescription("A set of protocols and tools for building software applications. APIs define how software components should interact.")
                .build());
        api.addTranslation(AbbreviationTranslation.builder()
                .languageCode("de")
                .shortDescription("Anwendungsprogrammierschnittstelle")
                .longDescription("Eine Sammlung von Protokollen und Werkzeugen zum Erstellen von Softwareanwendungen.")
                .build());
        abbreviationRepository.save(api);

        // REST
        Abbreviation rest = Abbreviation.builder().code("REST").build();
        rest.addTranslation(AbbreviationTranslation.builder()
                .languageCode("en")
                .shortDescription("Representational State Transfer")
                .longDescription("An architectural style for designing networked applications, relying on stateless, client-server communication via HTTP.")
                .build());
        rest.addTranslation(AbbreviationTranslation.builder()
                .languageCode("de")
                .shortDescription("Representational State Transfer")
                .longDescription("Ein Architekturstil für die Gestaltung von Netzwerkanwendungen.")
                .build());
        abbreviationRepository.save(rest);

        // HTTP
        Abbreviation http = Abbreviation.builder().code("HTTP").build();
        http.addTranslation(AbbreviationTranslation.builder()
                .languageCode("en")
                .shortDescription("Hypertext Transfer Protocol")
                .longDescription("The foundation of data communication on the World Wide Web. It defines how messages are formatted and transmitted.")
                .build());
        abbreviationRepository.save(http);

        // JSON
        Abbreviation json = Abbreviation.builder().code("JSON").build();
        json.addTranslation(AbbreviationTranslation.builder()
                .languageCode("en")
                .shortDescription("JavaScript Object Notation")
                .longDescription("A lightweight data interchange format that is easy for humans to read and write, and easy for machines to parse and generate.")
                .build());
        json.addTranslation(AbbreviationTranslation.builder()
                .languageCode("fr")
                .shortDescription("Notation Objet JavaScript")
                .longDescription("Un format d'echange de donnees leger, facile a lire et a ecrire pour les humains.")
                .build());
        abbreviationRepository.save(json);

        // SQL
        Abbreviation sql = Abbreviation.builder().code("SQL").build();
        sql.addTranslation(AbbreviationTranslation.builder()
                .languageCode("en")
                .shortDescription("Structured Query Language")
                .longDescription("A domain-specific language used for managing and querying relational databases.")
                .build());
        abbreviationRepository.save(sql);
    }
}
