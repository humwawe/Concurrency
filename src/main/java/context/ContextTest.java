package context;

import java.util.stream.IntStream;

/**
 * @author hum
 */
public class ContextTest {
    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i -> new Thread(new ExecutionTask()).start());
    }
}
