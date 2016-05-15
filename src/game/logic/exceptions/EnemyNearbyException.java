package game.logic.exceptions;

public class EnemyNearbyException extends Exception {
    public EnemyNearbyException() {
        super();
    }

    public EnemyNearbyException(String message) {
        super(message);
    }
}
