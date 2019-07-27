package lock.a.q.s;

import lock.bool.Lock;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author hum
 */
public class Test {
    public static void main(String[] args) {
        final AbstractQueuedSynchronizerLock aqsLock = new AbstractQueuedSynchronizerLock();
        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name -> new Thread(() -> {
                            try {
                                aqsLock.lock();
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                aqsLock.unlock();
                            }
                        }, name).start()
                );
        // 测试可重入
        M m = new M();
        new Thread(() -> {
            m.a();
        }).start();
    }

    private static void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is working...").ifPresent(System.out::println);
        Thread.sleep(2_000);
    }

    private static class M {
        final AbstractQueuedSynchronizerLock aqsLock = new AbstractQueuedSynchronizerLock();

        void a() {
            aqsLock.lock();
            System.out.println("a");
            b();
            aqsLock.unlock();
        }

        void b() {
            aqsLock.lock();
            System.out.println("b");
            aqsLock.unlock();
        }
    }
}
