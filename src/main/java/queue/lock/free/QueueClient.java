package queue.lock.free;


/**
 * @author hum
 */
public class QueueClient {
    public static void main(String[] args) {
        MyQueue<String> queue = new MyQueue<>();
        queue.addLast("11");
        queue.addLast("22");
        queue.addLast("33");
        System.out.println(queue.removeFirst());
        System.out.println(queue.removeFirst());
        System.out.println(queue.removeFirst());

        LockFreeQueue<String> queue1 = new LockFreeQueue<>();
        queue1.addLast("33");
        System.out.println(queue1.size());
        System.out.println(queue1.removeFirst());
        System.out.println(queue1.removeFirst());
        System.out.println(queue1.size());
    }
}
