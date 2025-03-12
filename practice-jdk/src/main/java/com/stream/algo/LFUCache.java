package com.stream.algo;

import java.util.HashMap;
import java.util.Map;

class LFUCache {

    // Map：key 到 Node
    // Map: freq 到 DummyNode
    // 双向链表：末尾节点的next 指向 dummy

    class Node {
        Node next, prev;
        int key;
        int val;
        int freq; // 频率

        public Node() {}

        public Node(int freq) {
            this.next = null;
            this.prev = null;
            this.freq = freq;
        }
    }

    int capacity;
    int size;
    Map<Integer, Node> keyToNode;
    Map<Integer, Node> freqToDummy;
    int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        keyToNode = new HashMap<>();
        freqToDummy = new HashMap<>();
        minFreq = 1;
    }


    public boolean remove(Node head, Node target) {
        // 如果移除完之后只剩下 dummy，删除 Map 中的相关映射，返回 true
        // 否则返回 false
        size --;
        if (target.next == head && target.prev == head) {
            freqToDummy.remove(target.freq);
            return true;
        }
        target.prev.next = target.next;
        target.next.prev = target.prev;
        return false;
    }

    public void add(Node head, Node target) {
        size ++;
        keyToNode.put(target.key, target);
        if (head.next == null) {
            head.next = target;
            target.next = head;
            target.prev = head;
            head.prev = target;
        } else {
            // 添加到链表末尾
            head.prev.next = target;
            target.prev = head.prev;
            target.next = head;
            head.prev = target;
        }
    }

    // 频率更新时，从原链表移除，加入到新链表
    // 并更新 freq2node 映射，但不涉及 key2node 映射
    public void removeAndInsert(Node node) {
        // 从原链表中移除
        Node dummy = freqToDummy.get(node.freq);
        if (remove(dummy, node) && minFreq == node.freq) {
            minFreq = node.freq + 1;
        }
        node.freq ++;
        // 插入到新链表
        Node newDummy = freqToDummy.get(node.freq);
        if (newDummy == null) {
            newDummy = new Node(node.freq);
            freqToDummy.put(node.freq, newDummy);
        }
        add(newDummy, node);
    }

    public int get(int key) {
        // key 是否存在 ？
        if (!keyToNode.containsKey(key)) return -1;
        Node node = keyToNode.get(key);
        removeAndInsert(node);
        return node.val;
    }

    public void put(int key, int value) {
        // key 是否存在？
        if (keyToNode.containsKey(key)) {
            // 若存在，则直接更新值、频率及位置
            // 更新 key2Node 映射
            Node node = keyToNode.get(key);
            node.val = value;
            keyToNode.put(key,node);

            removeAndInsert(node);

        } else {
            // size 是否已满？
            Node node = new Node();
            node.key = key;
            node.val = value;
            node.freq = 1;
            // 更新 key2Node 映射
            keyToNode.put(key, node);
            if (size == capacity) {
                // 已满
                // 剔除最久最不经常使用的节点
                Node targetDummy = freqToDummy.get(minFreq);
                Node removeNode = targetDummy.next;
                if (remove(targetDummy, removeNode)) {
                    minFreq = node.freq;
                }
                // 删除映射
                keyToNode.remove(removeNode.key);
            }
            // 插入到对应链表
            Node newDummy = freqToDummy.get(node.freq);
            if (newDummy == null) {
                newDummy = new Node(node.freq);
                freqToDummy.put(node.freq, newDummy);
            }
            add(newDummy, node);
            minFreq = 1;
        }
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */