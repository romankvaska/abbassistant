package com.chubb.abbassistant.exception;

public class TranslationNotFoundException extends RuntimeException {

    public TranslationNotFoundException(Long abbreviationId, String languageCode) {
        super("Translation not found for abbreviation id " + abbreviationId + " and language: " + languageCode);
    }
}
