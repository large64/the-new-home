package game.logic.exceptions;

/**
 * Created by Dénes on 2016. 05. 14..
 */
public class NotEnoughHousesException extends Exception {
    public NotEnoughHousesException() {
    }

    public NotEnoughHousesException(String message) {
        super(message);
    }
}
