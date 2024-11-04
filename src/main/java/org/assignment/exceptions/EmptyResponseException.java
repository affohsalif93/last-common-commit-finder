package org.assignment.exceptions;

public class EmptyResponseException extends RuntimeException {
    public EmptyResponseException() {
        super("Empty response");
    }
}
