package com.lamfire.chimaera.store.dbmstore;

import com.lamfire.chimaera.store.FireList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBMFireList implements FireList {
    private final List<byte[]> list;
    private JDBMEngine engine;
    private String name;
    private final Lock lock = new ReentrantLock();

    public DBMFireList(JDBMEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.list = engine.getLinkedList(name, new BytesSerializer());
    }

    @Override
    public void add(byte[] value) {
        try {
            lock.lock();
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
    public void set(int index, byte[] value) {
        list.set(index, value);
        engine.flush();
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
            byte[] bytes = list.remove(index);
            return bytes;
        } finally {
            lock.unlock();
            engine.flush();
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
            if(size() > 0){
                list.clear();
                engine.flush();
            }
        } finally {
            lock.unlock();

        }
    }

}
