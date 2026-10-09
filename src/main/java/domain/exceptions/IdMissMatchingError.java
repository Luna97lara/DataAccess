package domain.exceptions;

public class IdMissMatchingError extends RuntimeException {
    public IdMissMatchingError(String message) {
        super(message);
    }
}
