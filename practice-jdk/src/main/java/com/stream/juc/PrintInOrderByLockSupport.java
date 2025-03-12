package com.stream.juc;

import java.util.concurrent.locks.LockSupport;

public class PrintInOrderByLockSupport {
    private void printA(Thread thread) {
        try {
            Thread.sleep(10L);
            System.out.println("A");
            LockSupport.unpark(thread);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    private void printB(Thread thread) {
        try {
            Thread.sleep(10L);
            LockSupport.park();
            System.out.println("B");
            LockSupport.unpark(thread);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    private void printC() {
        try {
            Thread.sleep(10L);
            LockSupport.park();
            System.out.println("C");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PrintInOrderByLockSupport lockSupportTest = new PrintInOrderByLockSupport();
        Thread t3 = new Thread(()->{lockSupportTest.printC();});
        Thread t2 = new Thread(()->{lockSupportTest.printB(t3);});
        Thread t1 = new Thread(()->{lockSupportTest.printA(t2);});
        t1.start();
        t2.start();
        t3.start();
    }
}
