package com.stream.juc.aqs;

import com.sun.xml.internal.bind.marshaller.NoEscapeHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class MyLock {

    Thread owner;

    AtomicReference<Node> head = new AtomicReference<>(new Node());
    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    // 锁的可重入实现：因为重入可能是在函数递归时发生的，所以一定要计数
    AtomicInteger count = new AtomicInteger(0);

    static class Node {
        Node pre;
        Node next;
        Thread thread;
    }

    public void lock() {
        // 加锁
        // 如果拿到锁，返回，没拿到，阻塞
        // 即：如果把 flag 从 false 改成 true 成功，则返回，否则阻塞
        // 浅浅先试一下
        // 非公平锁：因为先试一下再排队，所以这其实就是一个插队的机会
        // 如果要改成公平锁，直接注释掉这一段即可，即任何人都得先进链表排队
        if (count.get() != 0) {
            if (owner == Thread.currentThread()) {
                // 如果锁是自己的
                // 直接返回
                count.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + "重入了锁");
                return;
            }
        } else {
            if (count.compareAndSet(0, 1)) {
                System.out.println(Thread.currentThread().getName() + "直接拿到锁");
                owner = Thread.currentThread();
                return;
            }
        }
        // 不成功，阻塞，加入到链表中
        Node current = new Node();
        current.thread = Thread.currentThread();
        // 问题：如何把当前节点放到尾节点上（因为可能有多个节点在加入尾节点，如果自己认为的tail不是真正的tail就把其他节点覆盖了）
        while (true) {
            Node currentTail = tail.get();
            if (tail.compareAndSet(currentTail, current)) {
                System.out.println(Thread.currentThread().getName() + "加入到链表尾");
                current.pre = currentTail;
                currentTail.next = current;
                break;
            }
        }
        // 可能出现虚假唤醒，如果被唤醒了，还需要检查自己是不是对的节点（头节点的下一个节点）
        // 如果不是，自己再阻塞自己
        while (true) {
            // LockSupport.park 不能放在这里，因为从加锁失败到阻塞期间有个时间差，锁可能在这期间就被释放了，在阻塞之前已经有人叫过了
            // 所以 park 之前要再确认一遍确实拿不到锁再阻塞
            if (current.pre == head.get() && count.compareAndSet(0, 1)) {
                // 是我该醒没错，加上自己的锁
                owner = Thread.currentThread();
                head.set(current); // 醒的只有我自己，0人竞争，所以这里不需要CAS改头节点
                current.pre.next = null;
                current.pre = null;
                System.out.println(Thread.currentThread().getName() + "被唤醒之后，拿到锁");
                break;
            }
            // 不是我，继续阻塞
            LockSupport.park();
        }
    }

    public void unlock() throws InterruptedException {
        // 解锁，只能解自己的锁
        if (owner != Thread.currentThread()) {
            throw new InterruptedException("当前线程没有锁！");
        }

        int i = count.get();
        if (i > 1) {
            count.decrementAndGet();
            System.out.println(Thread.currentThread().getName() + "解锁了重入锁");
            return;
        }

        Node headNode = head.get();
        Node next = headNode.next;
        // 注意头节点的获取一定要在释放锁之前
        // 否则释放锁之后拿到的头节点就不是真正的头节点了

        count.set(0);
//        owner = null;

        if (next != null) {
            // 唤醒下一个线程
            System.out.println(Thread.currentThread().getName() + "唤醒了" + next.thread.getName());
            LockSupport.unpark(next.thread);
        }
        // 没有下一个线程，那不管了
    }
}
