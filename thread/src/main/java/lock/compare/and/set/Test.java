package lock.compare.and.set;

/**
 * @author hum
 */
public class Test {
    private final static CompareAndSetLock LOCK = new CompareAndSetLock();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        doSomething();
                    } catch (GetLockException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void doSomething() throws GetLockException, InterruptedException {
        try {
            LOCK.tryLock();
            System.out.println(Thread.currentThread().getName() + " get the lock");
            Thread.sleep(10_000);
        } finally {
            LOCK.unlock();
        }


    }
}
