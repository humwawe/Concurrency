package produce.consume.v3;

import java.util.stream.IntStream;

/**
 * @author hum
 */
public class Client {
    public static void main(String[] args) {
        ConditionQueue conditionQueue = new ConditionQueue();
        Producer producer = new Producer(conditionQueue);
        Consumer consumer = new Consumer(conditionQueue);
        IntStream.range(0, 2).forEach(producer::beginProduce);
        IntStream.range(0, 1).forEach(consumer::beginConsume);

    }
}
