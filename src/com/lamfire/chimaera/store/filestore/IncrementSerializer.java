package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.memstore.FireIncrementInMemory;
import org.apache.jdbm.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-24
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class IncrementSerializer implements Serializer<FireIncrement> {

    @Override
    public void serialize(DataOutput dataOutput, FireIncrement inc) throws IOException {
        dataOutput.writeLong(inc.get());
    }

    @Override
    public FireIncrement deserialize(DataInput dataInput) throws IOException, ClassNotFoundException {
        long val = dataInput.readLong();
        FireIncrement inc = new FireIncrementInMemory();
        inc.set(val);
        return inc;
    }
}
