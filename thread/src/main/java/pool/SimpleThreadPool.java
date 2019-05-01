package pool;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hum
 */
public class SimpleThreadPool extends Thread {
    private int size;
    private final int queueSize;
    private final static int DEFAULT_TASK_QUEUE_SIZE = 200;
    private static volatile int seq;
    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";
    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();
    private final static List<WorkTask> THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;
    private final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard this task");
    };

    private volatile boolean destroy = false;

    private int min;
    private final static int DEFAULT_MIN = 4;
    private int active;
    private final static int DEFAULT_ACTIVE = 8;
    private int max;
    private final static int DEFAULT_MAX = 12;

    public SimpleThreadPool(int min, int active, int max, int queueSize, DiscardPolicy discardPolicy) {
        this.min = min;
        this.active = active;
        this.max = max;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    public SimpleThreadPool() {
        this(DEFAULT_MIN, DEFAULT_ACTIVE, DEFAULT_MAX, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }


    @Override
    public void run() {
        while (!destroy) {
            System.out.printf("Pool#Min:%d,Active:%d,Max:%d,Current:%d,QueueSize:%d\n", min, active, max, size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000);
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        createWorkTask();
                    }
                    System.out.println("The Pool incremented to active");
                    size = active;
                } else if (TASK_QUEUE.size() > max && size < max) {
                    for (int i = size; i < max; i++) {
                        createWorkTask();
                    }
                    System.out.println("The Pool incremented to max");
                    size = max;
                }
                synchronized (THREAD_QUEUE) {
                    synchronized (TASK_QUEUE) {
                        if (TASK_QUEUE.isEmpty() && size > active) {
                            System.out.println("The Pool is reducing");
                            int releaseSize = size - active;
                            while (releaseSize > 0 && THREAD_QUEUE.size() > 0) {
                                for (Iterator<WorkTask> it = THREAD_QUEUE.iterator(); it.hasNext(); ) {
                                    if (releaseSize <= 0) {
                                        break;
                                    }
                                    WorkTask task = it.next();
                                    if (task.getTaskState() == TaskState.FREE || task.getTaskState() == TaskState.BLOCKED) {
                                        task.interrupt();
                                        task.close();
                                        it.remove();
                                        releaseSize--;
                                    }
                                }
                                size = active;
                            }
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void init() {
        for (int i = 0; i < min; i++) {
            createWorkTask();
        }
        this.size = min;
        this.start();
    }

    private void createWorkTask() {
        WorkTask task = new WorkTask(GROUP, THREAD_PREFIX + (seq++));
        task.start();
        THREAD_QUEUE.add(task);
    }

    public void submit(Runnable runnable) {
        if (destroy) {
            throw new IllegalStateException("The thread pool already destroy and not allow submit task");
        }
        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > queueSize) {
                discardPolicy.discard();
            }
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }


    public void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(50);
        }
        synchronized (THREAD_QUEUE) {
            int initVal = THREAD_QUEUE.size();
            while (initVal > 0) {
                for (WorkTask task : THREAD_QUEUE) {
                    if (task.getTaskState() == TaskState.BLOCKED) {
                        task.interrupt();
                        //THREAD_QUEUE没有出队的操作，close 使得状态为DEAD，不会因为BLOCKED再次判断
                        task.close();
                        initVal--;
                    } else {
                        Thread.sleep(10);
                    }
                }
            }
        }
        THREAD_QUEUE.clear();
        this.destroy = true;
        System.out.println("The thread pool disposed");
    }

    public int getSize() {
        return size;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public boolean isDestroy() {
        return destroy;
    }


    private static class WorkTask extends Thread {
        private volatile TaskState taskState = TaskState.FREE;

        public WorkTask(ThreadGroup group, String name) {
            super(group, name);
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        @Override
        public void run() {
            OUTER:
            while (this.taskState != TaskState.DEAD) {
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Closed " + Thread.currentThread());
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }
                if (runnable != null) {
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    taskState = TaskState.FREE;
                }
            }
        }

        public void close() {
            taskState = TaskState.DEAD;
        }
    }


}
