package game.logic.exceptions;

/**
 * Created by Dénes on 2016. 05. 14..
 */
public class EnemyNearbyException extends Exception {
    public EnemyNearbyException() {
        super();
    }

    public EnemyNearbyException(String message) {
        super(message);
    }
}
