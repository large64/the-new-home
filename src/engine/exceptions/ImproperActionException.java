package engine.exceptions;

/**
 * Created by Dénes on 2015. 11. 23..
 */
public class ImproperActionException extends RuntimeException {
    public ImproperActionException() {
        super();
    }

    public ImproperActionException(String string) {
        super(string);
    }
}
