package com.stream.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 按序打印
 * 1. reentrantLock,
 * 2. Semaphore,
 */
public class PrintInOrder {
    public static void first() {
        System.out.println("1");
    }
    public static void second() {
        System.out.println("2");
    }
    public static void third() {
        System.out.println("3");
    }

    /**
     * 1. reentrantLock
     */
    public static void printInOrderByLock() {

    }



    /**
     * 2. Semaphore
     */
    public static void printInOrderBySemaphore() {
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(0);
        Semaphore semaphore3 = new Semaphore(0);
        new Thread(() -> {
            try {
                semaphore1.acquire();
                first();
                semaphore2.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                semaphore2.acquire();
                second();
                semaphore3.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                semaphore3.acquire();
                third();
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void main(String[] args) {
//        printInOrderBySemaphore();
    }
}
