package produce.consume.v1;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author hum
 */
public class CaptureService {

    private final static int MAX_WORKERS = 5;
    final static private LinkedList<Control> CONTROLS = new LinkedList<>();

    public static void main(String[] args) {
        List<Thread> worker = new ArrayList<>();
        Stream.of("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
                .map(CaptureService::creteCaptureThread)
                .forEach(t -> {
                    t.start();
                    worker.add(t);
                });
        worker.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Optional.of("All capture worker finished").ifPresent(System.out::println);
    }

    private static Thread creteCaptureThread(String name) {
        return new Thread(() -> {
            Optional.of("The worker [" + Thread.currentThread().getName() + "] BEGIN capture data").ifPresent(System.out::println);
            synchronized (CONTROLS) {
                // 用while使得被唤醒（notifyall）都要去重新判断条件
                while (CONTROLS.size() >= MAX_WORKERS) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONTROLS.add(new Control());
            }

            Optional.of("The worker [" + Thread.currentThread().getName() + "] is working....").ifPresent(System.out::println);
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (CONTROLS) {
                Optional.of("The worker [" + Thread.currentThread().getName() + "] END capture data ").ifPresent(System.out::println);
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }

    private static class Control {
    }
}
