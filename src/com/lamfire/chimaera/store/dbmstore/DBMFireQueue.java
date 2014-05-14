package com.lamfire.chimaera.store.dbmstore;

import com.lamfire.chimaera.store.FireQueue;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBMFireQueue implements FireQueue {

    private final List<byte[]> list;
    private JDBMEngine engine;
    private String name;
    private final Lock lock = new ReentrantLock();

    public DBMFireQueue(JDBMEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.list = engine.getLinkedList(name, new BytesSerializer());
    }

    @Override
    public synchronized void push(byte[] value) {
        lock.lock();
        try {
            list.add(value);
        } finally {
            lock.unlock();
            engine.flush();
        }
    }

    public Iterator<byte[]> iterator() {
        return list.iterator();
    }

    @Override
    public synchronized byte[] pop() {
        lock.lock();
        try {
            if (list.isEmpty()) {
                return null;
            }
            return list.remove(0);
        } finally {
            lock.unlock();
            engine.flush();
        }
    }

    public synchronized byte[] peek() {
        lock.lock();
        try {
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            list.clear();
            engine.flush();
        } finally {
            lock.unlock();

        }
    }

}
