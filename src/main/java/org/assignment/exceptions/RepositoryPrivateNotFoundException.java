package org.assignment.exceptions;

public class RepositoryPrivateNotFoundException extends IllegalArgumentException {
    public RepositoryPrivateNotFoundException() {
        super("Repository doesn't exists or is private");
    }
}
