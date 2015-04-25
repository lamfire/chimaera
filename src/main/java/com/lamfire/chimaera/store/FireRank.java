package com.lamfire.chimaera.store;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 上午11:47
 * To change this template use File | Settings | File Templates.
 */
public interface FireRank extends FireCollection {
    public void put(String name);

    public void incr(String name, long step);

    public void set(String name, long score);

    public long score(String name);

    public long remove(String name);

    public List<Item> max(int size);

    public List<Item> min(int size);

    public List<Item> maxRange(int from, int size);

    public List<Item> minRange(int from, int size);
}
