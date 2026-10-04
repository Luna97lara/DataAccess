package domain.exceptions;

public class DataBaseError extends RuntimeException{
    public DataBaseError(String message){
        super(message);
    }
}
