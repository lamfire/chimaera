package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryFireList implements FireList {
    private final Lock lock = new ReentrantLock();
    private final List<byte[]> list = new LinkedList<byte[]>();

    @Override
    public void add(byte[] value) {
        try {
            lock.lock();
            list.add(value);
        } finally {
            lock.unlock();
        }

    }

    public Iterator<byte[]> iterator() {
        return list.iterator();
    }

    @Override
    public void set(int index, byte[] value) {
        list.set(index, value);
    }

    @Override
    public byte[] get(int index) {
        return list.get(index);
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> result = new ArrayList<byte[]>();
        try {
            lock.lock();
            for (int i = fromIndex; i < list.size(); i++) {
                result.add(list.get(i));
                if (result.size() >= size) {
                    break;
                }
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] remove(int index) {
        try {
            lock.lock();
            return list.remove(index);
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
        try {
            lock.lock();
            list.clear();
        } finally {
            lock.unlock();
        }
    }

}
