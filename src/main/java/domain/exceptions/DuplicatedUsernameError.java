package domain.exceptions;

public class DuplicatedUsernameError extends RuntimeException {
    public DuplicatedUsernameError(String message) {
        super(message);
    }
}
