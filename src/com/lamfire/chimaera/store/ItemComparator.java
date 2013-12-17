package com.lamfire.chimaera.store;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-8
 * Time: 上午11:25
 * To change this template use File | Settings | File Templates.
 */
public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        if(o1 == o2){
            return 0;
        }
        if(o1.getValue() > o2.getValue()){
            return -1;
        }
        if(o1.getValue() == o2.getValue()){
            if(o1.hashCode() == o2.hashCode()){
                return 0;
            }
            if(o1.hashCode() > o2.hashCode()){
                return -1;
            }
        }
        return 1;
    }
}
