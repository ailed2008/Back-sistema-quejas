package com.banco.app_quejas.exception;

public class ComplaintNotFoundException extends RuntimeException {

    public ComplaintNotFoundException(String folio) {
        super("No complaint found with folio: '" + folio + "'.");
    }
}
