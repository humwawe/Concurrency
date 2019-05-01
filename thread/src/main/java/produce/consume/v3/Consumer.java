package produce.consume.v3;

/**
 * @author hum
 */
public class Consumer {
    ConditionQueue conditionQueue = new ConditionQueue();

    public Consumer(ConditionQueue conditionQueue) {
        this.conditionQueue = conditionQueue;
    }

    public void beginConsume(int i) {
        new Thread(() -> {
            while (true) {
                conditionQueue.consume();
                conditionQueue.sleep(5);
            }
        }, "C-" + i).start();
    }

}
