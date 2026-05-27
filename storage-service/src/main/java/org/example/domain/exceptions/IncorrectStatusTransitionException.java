package org.example.domain.exceptions;

public class IncorrectStatusTransitionException extends RuntimeException {
    public IncorrectStatusTransitionException(String message) {
        super(message);
    }
}
