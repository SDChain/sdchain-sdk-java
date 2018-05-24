package io.sdchain.exception;

public class RequestException extends SDChainException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException(Throwable e) {
        super(e.getMessage(), e);
    }
    
    public RequestException(String message, Throwable e) {
        super(message, e);
    }

    private static final long serialVersionUID = 1L;

}
