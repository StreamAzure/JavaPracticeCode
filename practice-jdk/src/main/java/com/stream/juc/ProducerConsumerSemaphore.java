package com.stream.juc;

import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore实现生产者消费者模型
 */
public class ProducerConsumerSemaphore {
    private static int count = 0;
    private static final int FULL = 10;
    private static Semaphore notFull = new Semaphore(10);
    private static Semaphore notEmpty = new Semaphore(0);
    private static Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    notFull.acquire();
                    mutex.acquire();
                    count++;
                    System.out.println("生产者生产，目前总共有：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notEmpty.release();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    notEmpty.acquire();
                    mutex.acquire();
                    count--;
                    System.out.println("消费者消费，目前总共有：" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notFull.release();
                }
            }
        }
    }
    
}
