package org.assignment.exceptions;

public class RateLimitOrPermissionException extends RuntimeException {
    public RateLimitOrPermissionException() {
        super("Insufficient permissions or rate limit exceeded");
    }
}
