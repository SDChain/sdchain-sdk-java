package io.sdchain.exception;

public class FailedException extends SDChainException {
    private static final long serialVersionUID = 1L;

    public FailedException(String message) {
        super(message);
    }
}
