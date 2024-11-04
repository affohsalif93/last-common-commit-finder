package org.assignment.exceptions;

public class HttpRequestException extends RuntimeException {
    public HttpRequestException(int statusCode) {
        super("HTTP request failed with status code " + statusCode);
    }
}
