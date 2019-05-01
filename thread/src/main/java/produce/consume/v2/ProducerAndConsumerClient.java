package produce.consume.v2;

/**
 * @author hum
 */
public class ProducerAndConsumerClient {

    public static void main(String[] args) {
        final MessageQueue messageQueue = new MessageQueue();
        new ProducerThread(messageQueue, 1).start();
        new ProducerThread(messageQueue, 2).start();
        new ProducerThread(messageQueue, 3).start();
        new ConsumerThread(messageQueue, 1).start();
        new ConsumerThread(messageQueue, 2).start();
    }
}
