package game.logic.exceptions;

public class NotEnoughHousesException extends Exception {
    public NotEnoughHousesException() {
    }

    public NotEnoughHousesException(String message) {
        super(message);
    }
}
