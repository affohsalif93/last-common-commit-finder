package org.assignment.exceptions;

public class InconsistentCommitHistoryException extends RuntimeException {
    public InconsistentCommitHistoryException() {
        super("Inconsistent commit history");
    }
}
