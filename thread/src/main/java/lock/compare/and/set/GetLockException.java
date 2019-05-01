package lock.compare.and.set;

/**
 * @author hum
 */
public class GetLockException extends Exception {
    public GetLockException(String message) {
        super(message);
    }

    public GetLockException() {

    }
}
