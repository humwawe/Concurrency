package queue.lock.free;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hum
 */
public class LockFreeQueue<E> {
    private static class Node<E> {
        private E element;
        volatile private Node<E> next;

        public Node(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return element == null ? "" : element.toString();
        }
    }

    private AtomicReference<Node<E>> head, tail;
    private AtomicInteger size = new AtomicInteger(0);

    public LockFreeQueue() {
        Node<E> node = new Node<>(null);
        this.head = new AtomicReference<>(node);
        this.tail = new AtomicReference<>(node);
    }

    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException("null element");
        }
        Node<E> newNode = new Node<>(e);
        Node<E> preNode = tail.getAndSet(newNode);
        preNode.next = newNode;
        size.incrementAndGet();
    }

    public E removeFirst() {
        Node<E> headNode, ValueNode;
        do {
            headNode = head.get();
            ValueNode = headNode.next;
        } while (ValueNode != null && !head.compareAndSet(headNode, ValueNode));
        E value = (ValueNode != null ? ValueNode.element : null);
        if (ValueNode != null) {
            ValueNode.element = null;
            size.decrementAndGet();
        }
        return value;
    }

    public int size() {
        return size.get();
    }

}
