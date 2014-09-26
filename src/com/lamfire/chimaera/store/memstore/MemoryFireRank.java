package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.store.ItemComparator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public class MemoryFireRank implements FireRank {
    private final TreeSet<Item> set = new TreeSet<Item>(new ItemComparator());
    private final HashMap<String, Item> items = new HashMap<String, Item>();
    private final Lock lock = new ReentrantLock();

    @Override
    public synchronized void put(String name) {
        incr(name, 1);
    }

    @Override
    public void incr(String name, long step) {
        lock.lock();
        try {
            Item item = items.get(name);
            if (item == null) {
                item = new Item(name);
                item.increment(step);
                items.put(name, item);
                set.add(item);
                return;
            }
            set.remove(item);
            item.increment(step);
            set.add(item);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void set(String name, long count) {
        lock.lock();
        try {
            Item item = items.get(name);
            if (item == null) {
                item = new Item();
                item.setName(name);
                item.setValue(count);
                items.put(name, item);
                set.add(item);
                return;
            }
            set.remove(item);
            item.setValue(count);
            set.add(item);
        } finally {
            lock.unlock();
        }
    }

    public Iterator<Item> iterator() {
        return set.iterator();
    }

    @Override
    public long score(String name) {
        Item item = items.get(name);
        if (item == null) {
            return 0;
        }
        return item.getValue();
    }

    @Override
    public synchronized long remove(String name) {
        lock.lock();
        try {
            Item item = items.remove(name);
            if (item == null) {
                return 0;
            }
            set.remove(item);
            return item.getValue();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> max(int size) {
        try {
            lock.lock();
            if (set.isEmpty()) {
                return null;
            }
            return subList(set.iterator(), 0, size);
        } finally {
            lock.unlock();
        }
    }

    private synchronized List<Item> subList(Iterator<Item> it, int from, int size) {

        try {
            lock.lock();
            List<Item> result = new ArrayList<Item>();
            int count = 0;
            int index = 0;
            while (it.hasNext()) {
                Item item = it.next();
                if (index >= from) {
                    result.add(item);
                    count++;
                    if (count >= size) {
                        break;
                    }
                }
                index++;
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> min(int size) {
        try {
            lock.lock();
            if (set.isEmpty()) {
                return null;
            }
            return subList(set.descendingIterator(), 0, size);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> maxRange(int from, int size) {
        try {
            lock.lock();
            if (set.isEmpty()) {
                return null;
            }
            return subList(set.iterator(), from, size);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> minRange(int from, int size) {
        try {
            lock.lock();
            if (set.isEmpty()) {
                return null;
            }
            return subList(set.descendingIterator(), from, size);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        return set.size();
    }

    @Override
    public synchronized void clear() {
        lock.lock();
        try {
            this.set.clear();
            this.items.clear();
        } finally {
            lock.unlock();
        }
    }
}
