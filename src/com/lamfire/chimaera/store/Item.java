package com.lamfire.chimaera.store;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-8
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class Item{
    private String name;
    private AtomicLong count = new AtomicLong();

    public Item(){

    }

    public Item(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public long getValue() {
        return count.get();
    }

    public void setValue(long count){
        this.count.set(count);
    }

    public long increment(){
        return count.incrementAndGet();
    }

    public long increment(long step){
        return count.addAndGet(step);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
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

    public int compareTo(Item o) {
        if(this.equals(o)){
            return 0;
        }

        if(this.getValue() > o.getValue()){
            return 1;
        }

        if(this.getValue() == o.getValue()){
            if(this.hashCode() == o.hashCode()){
                return 0;
            }
            if(this.hashCode() > o.hashCode()){
                return 1;
            }
        }

        return -1;
    }
}

