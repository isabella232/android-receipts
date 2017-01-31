package tech.receipts.exception;

import java.util.Collection;

import tech.receipts.components.validation.Issue;

public class TicketValidationException extends Exception {

    private final Collection<Issue> issues;

    public TicketValidationException(Collection<Issue> issues) {
        super();

        this.issues = issues;
    }

    public Collection<Issue> getIssues() {
        return issues;
    }
}
