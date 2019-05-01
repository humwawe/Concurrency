package pool;

import java.util.stream.IntStream;

/**
 * @author hum
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool();
        IntStream.rangeClosed(0, 40)
                .forEach(i -> {
                    threadPool.submit(() -> {
                        System.out.println("The runnable " + i + " be serviced by " + Thread.currentThread() + " start");
                        try {
                            Thread.sleep(3_000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("The runnable " + i + " be serviced by " + Thread.currentThread() + " finished");
                    });
                });
        //Thread.sleep(2_000);
        //threadPool.shutdown();
    }
}
