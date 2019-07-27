package stop;

/**
 * @author hum
 */
public class ThreadService {
    private Thread executeThread;

    //不加volatile ，即使没超时也会报任务超时，shutdown看不到finished的变化
    private volatile boolean finished = false;

    public void execute(final Runnable task) {
        executeThread = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                // 守护线程去执行任务，setDaemon：当执行线程结束，守护线程也结束
                runner.setDaemon(true);
                runner.start();
                try {
                    runner.join();
                    finished = true;
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        };
        executeThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if ((System.currentTimeMillis() - currentTime) >= mills) {
                System.out.println("任务超时，需要结束!!");
                executeThread.interrupt();
                break;
            }

        }

        finished = false;
    }
}
