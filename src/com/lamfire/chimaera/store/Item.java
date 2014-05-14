package com.lamfire.chimaera.store;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-8
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class Item implements Serializable {
    private String name;
    private AtomicLong count = new AtomicLong();

    public Item() {

    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return count.get();
    }

    public void setValue(long count) {
        this.count.set(count);
    }

    public long increment() {
        return count.incrementAndGet();
    }

    public long increment(long step) {
        return count.addAndGet(step);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Item other = (Item) obj;
        return this.name.equals(other.getName());
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", score=" + count +
                '}';
    }
}


