package lock.bool;

import java.util.*;

/**
 * @author hum
 */
public class BooleanLock implements Lock {
    private boolean initValue;

    private Collection<Thread> blockedThreadCollection = new HashSet<>();

    // 记录是谁锁的，只有拿锁的线程能够释放锁
    private Thread currentThread;

    public BooleanLock() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        while (initValue) {
            blockedThreadCollection.add(Thread.currentThread());
            this.wait();
        }
        blockedThreadCollection.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
        Optional.of(Thread.currentThread().getName() + " get the lock monitor").ifPresent(System.out::println);
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeOutException {
        if (mills <= 0) {
            lock();
        }
        long hasRemaining = mills;
        long endTime = System.currentTimeMillis() + mills;
        while (initValue) {
            if (hasRemaining <= 0) {
                throw new TimeOutException("Time out");
            }
            blockedThreadCollection.add(Thread.currentThread());
            this.wait(hasRemaining);
            hasRemaining = endTime - System.currentTimeMillis();
        }
        blockedThreadCollection.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
        Optional.of(Thread.currentThread().getName() + " get the lock monitor").ifPresent(System.out::println);
    }

    @Override
    public synchronized void unlock() {
        if (Thread.currentThread() == currentThread) {
            this.initValue = false;
            Optional.of(Thread.currentThread().getName() + " release the lock monitor").ifPresent(System.out::println);
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        return Collections.unmodifiableCollection(blockedThreadCollection);
    }

    @Override
    public int getBlockedSize() {
        return blockedThreadCollection.size();
    }
}
