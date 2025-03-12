package com.stream.juc.aqs;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int[] count = new int[] {1000};
        List<Thread> threadList = new ArrayList<>();
        MyLock lock = new MyLock();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    lock.lock(); // 可重入锁测试
                    count[0] --;
                    try {
                        lock.unlock();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(count[0]);
            }));
        }
        threadList.forEach(Thread::start);
        for (Thread thread : threadList) {
            thread.join();
        }
        System.out.println(count[0]);
    }
}
