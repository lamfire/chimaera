package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.FireIncrement;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-7-14
 * Time: 下午7:14
 * To change this template use File | Settings | File Templates.
 */
public class Sequence {
    private FireIncrement sequenceTable;
    private String name;

    public Sequence(FireIncrement table,String name){
        this.sequenceTable = table;
        this.name = name;
    }

    public long get(){
        return sequenceTable.get(name);
    }

    public long inrc(int step){
        return sequenceTable.incrGet(name,step);
    }

    public void set(long value){
        sequenceTable.set(name,value);
    }
}
