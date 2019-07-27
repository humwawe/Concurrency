package pool;

/**
 * @author hum
 */
public interface DiscardPolicy {
    void discard() throws DiscardException;
}
