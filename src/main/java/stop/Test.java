package stop;

/**
 * @author hum
 */
public class Test {
    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(() -> {
            // 1. long time
            //while (true) {
            //}
            // 2. 2s
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown(7_000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
