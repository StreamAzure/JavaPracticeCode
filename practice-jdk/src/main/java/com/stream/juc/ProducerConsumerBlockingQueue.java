package com.stream.juc;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 实现容量为100的生产者消费者模型
 */
public class ProducerConsumerBlockingQueue {
    private static ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(100, true);

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    arrayBlockingQueue.put(i);
                    // 模拟随机延迟
                    Thread.sleep((int) (Math.random() * 100));
                    System.out.println("生产者生产：" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    Integer take = arrayBlockingQueue.take();
                    // 模拟延迟
                    Thread.sleep(100);
                    System.out.println("消费者消费：" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
