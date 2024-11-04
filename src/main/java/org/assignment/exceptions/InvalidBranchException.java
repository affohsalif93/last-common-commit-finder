package org.assignment.exceptions;

import java.util.List;

public class InvalidBranchException extends IllegalArgumentException {
    public InvalidBranchException(String branch, List<String> branches) {
        super("Invalid branch " + branch + ". Should be one of: " + branches);
    }
}
