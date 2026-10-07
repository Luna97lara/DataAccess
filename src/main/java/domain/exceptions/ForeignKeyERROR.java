package domain.exceptions;

public class ForeignKeyERROR extends RuntimeException {
    public ForeignKeyERROR(String message) {
        super(message);
    }
}
