package future;

/**
 * @author hum
 */
public interface Future<T> {

    T get() throws InterruptedException;

}
