package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireQueue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryFireQueue implements FireQueue {
    private final Lock lock = new ReentrantLock();
    private final LinkedList<byte[]> queue = new LinkedList<byte[]>();

    public void push(byte[] value) {
        try {
            lock.lock();
            queue.addLast(value);
        } finally {
            lock.unlock();
        }
    }

    public Iterator<byte[]> iterator() {
        return queue.iterator();
    }

    @Override
    public byte[] pop() {
        try {
            lock.lock();
            if (queue.isEmpty()) {
                return null;
            }
            return queue.removeFirst();
        } finally {
            lock.unlock();
        }
    }

    public byte[] peek() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.peekFirst();
    }

    @Override
    public long size() {
        return queue.size();
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            queue.clear();
        } finally {
            lock.unlock();
        }
    }

}
