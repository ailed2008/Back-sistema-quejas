package com.banco.app_quejas.exception;

public class DuplicateFolioException extends RuntimeException {

    public DuplicateFolioException(String folio) {
        super("A complaint with folio '" + folio + "' already exists in the system.");
    }
}
