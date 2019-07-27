package produce.consume.v3;

/**
 * @author hum
 */
public class Producer {
    ConditionQueue conditionQueue = new ConditionQueue();

    public Producer(ConditionQueue conditionQueue) {
        this.conditionQueue = conditionQueue;
    }

    public void beginProduce(int i) {
        new Thread(() -> {
            while (true) {
                conditionQueue.produce();
                conditionQueue.sleep(1);
            }
        }, "P-" + i).start();
    }

}
