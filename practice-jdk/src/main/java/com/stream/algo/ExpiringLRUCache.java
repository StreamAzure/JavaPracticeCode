package com.stream.algo;

import java.util.*;

/**
 * 带过期时间的LRU
 * 定时删除+懒删除
 *
 */
public class ExpiringLRUCache {
    private final Map<Integer, Node> keyToNode;
    private final Node dummy;
    private final int capacity;
    private int size;

    private static class Node {
        int key, value;
        long ttl;
        Node prev, next;

        public Node(){}
        public Node(int key, int value, long ttl) {
            this.key = key;
            this.value = value;
            this.ttl = System.currentTimeMillis() + ttl;
        }
        public boolean isExpired() {
            return System.currentTimeMillis() > ttl;
        }
    }

    public ExpiringLRUCache(int capacity) {
        keyToNode = new HashMap<>();
        dummy = new Node();
        dummy.next = dummy;
        dummy.prev = dummy;
        this.capacity = capacity;
        this.size = 0;
        Timer watchDog = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时删除中……");
                synchronized (dummy) {
                    Node p = dummy.next;
                    while (p != dummy) {
                        if (p.isExpired()) {
                            Node t = p;
                            p = p.next;
                            System.out.println("删除过期 Node, key = " + t.key);
                            keyToNode.remove(t.key);
                            remove(t);
                        } else {
                            p = p.next;
                        }
                    }
                }
            }
        };
        watchDog.schedule(timerTask, 0, 1000);
    }

    public int get(int key) {
        synchronized (dummy) {
            // 检查是否存在
            if (!keyToNode.containsKey(key)) {
                System.out.println("get key = " + key + " 不存在");
                return -1;
            }
            // 检查是否过期，若过期就顺手删了
            Node node = keyToNode.get(key);
            if (node.isExpired()) {
                System.out.println("get key = " + node.key + " 已过期，删除");
                keyToNode.remove(key);
                remove(node);
                return -1;
            }
            int x = node.value;
            remove(node);
            insert(node);
            System.out.println("get key = " + node.key + ", value = " + node.value);
            return x;
        }
    }

    public void put(int key, int value, long ttl) {
        synchronized (dummy) {
            // 判断是否存在，存在则更新
            if (keyToNode.containsKey(key)) {
                // 这里不需要管是否过期
                Node node = keyToNode.get(key);
                node.value = value;
                node.ttl = System.currentTimeMillis() + ttl;
                remove(node);
                insert(node);
                System.out.println("key = " + node.key + " 更新成功");
            } else {
                // 不存在
                Node node = new Node(key, value, ttl);
                // 判断 size 是否已满
                if (size == capacity) {
                    // 已满，踢一个
                    Node removeNode = dummy.next;
                    keyToNode.remove(removeNode.key);
                    remove(removeNode);
                }
                insert(node);
                keyToNode.put(key, node);
                System.out.println("key = " + node.key + ", value = " + node.value + " 插入成功");
            }
        }
    }

    private void insert(Node node) {
        size ++;
        dummy.prev.next = node;
        node.next = dummy;
        node.prev = dummy.prev;
        dummy.prev = node;
    }

    private void remove(Node node) {
        size --;
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }


    public static void main(String[] args) throws InterruptedException {
        ExpiringLRUCache expiringLRUCache = new ExpiringLRUCache(30);
        Random random = new Random();
        while(true) {
            int op = random.nextInt(2);
            Thread.sleep(random.nextInt(200));
            if (op == 1) {
                int key = random.nextInt(5);
                int value = random.nextInt(5000);
                long ttl = random.nextInt(3000);
                expiringLRUCache.put(key, value, ttl);
            } else {
                int key = random.nextInt(8);
                expiringLRUCache.get(key);
            }
        }
    }
}
