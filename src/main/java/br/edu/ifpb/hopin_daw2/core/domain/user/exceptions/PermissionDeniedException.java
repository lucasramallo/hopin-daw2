package br.edu.ifpb.hopin_daw2.core.domain.user.exceptions;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("Permission denied! The user doesn't have permission to do this action.");
    }
}
