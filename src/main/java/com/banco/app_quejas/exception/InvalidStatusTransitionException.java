package com.banco.app_quejas.exception;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(String from, String to) {
        super("Invalid status transition from '" + from + "' to '" + to + "'.");
    }
}
