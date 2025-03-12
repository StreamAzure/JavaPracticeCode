package com.stream.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现生产者消费者模型
 */
public class ProducerConsumerReentrantLock {
    private static int count = 0;
    private static final int FULL = 10;
    private static final String LOCK = "lock";
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Condition notFull = reentrantLock.newCondition();
    private static Condition notEmpty = reentrantLock.newCondition();

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // 加锁
                reentrantLock.lock();
                try {
                    // 当生产者生产时，如果容器满了，则等待
                    while (count == FULL) {
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println("生产者生产，目前总共有：" + count);
                    notEmpty.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            for (int i = 0;i < 10;i ++) {
                reentrantLock.lock();

            }
        }
    }

}
