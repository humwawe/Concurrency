package produce.consume.v3;


import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hum
 */
public class ConditionQueue {
    private final static Lock LOCK = new ReentrantLock();
    private final static Condition PRODUCE_COND = LOCK.newCondition();
    private final static Condition CONSUME_COND = LOCK.newCondition();
    private final static LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();
    private final static int MAX_CAPACITY = 10;

    public void produce() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.size() > MAX_CAPACITY) {
                PRODUCE_COND.await();
            }
            Long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-P-" + value);
            TIMESTAMP_POOL.addLast(value);

            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public void consume() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUME_COND.await();
            }
            Long value = TIMESTAMP_POOL.removeFirst();
            System.out.println(Thread.currentThread().getName() + "-C-" + value);

            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public void sleep(long sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
