package com.chubb.abbassistant.exception;

public class DuplicateAbbreviationException extends RuntimeException {

    public DuplicateAbbreviationException(String code) {
        super("Abbreviation already exists with code: " + code);
    }
}
