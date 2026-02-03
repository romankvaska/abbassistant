package com.chubb.abbassistant.exception;

public class AbbreviationNotFoundException extends RuntimeException {

    public AbbreviationNotFoundException(String message) {
        super(message);
    }

    public AbbreviationNotFoundException(Long id) {
        super("Abbreviation not found with id: " + id);
    }

    public static AbbreviationNotFoundException byCode(String code) {
        return new AbbreviationNotFoundException("Abbreviation not found with code: " + code);
    }
}
