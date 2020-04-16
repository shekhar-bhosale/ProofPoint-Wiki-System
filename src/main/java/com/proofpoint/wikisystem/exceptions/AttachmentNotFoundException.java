package com.proofpoint.wikisystem.exceptions;

/*
We may implement service specific not found exceptions as well. Doing this just for attachment for now
 */
public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(final String customerFacingErrorMessage) {
        super(customerFacingErrorMessage);
    }
}

