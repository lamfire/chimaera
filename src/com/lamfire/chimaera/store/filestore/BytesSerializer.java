package com.lamfire.chimaera.store.filestore;

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
public class BytesSerializer implements Serializer<byte[]> {

    @Override
    public void serialize(DataOutput dataOutput, byte[] bytes) throws IOException {
        dataOutput.writeInt(bytes.length);
        dataOutput.write(bytes);
    }

    @Override
    public byte[] deserialize(DataInput dataInput) throws IOException, ClassNotFoundException {
        int len = dataInput.readInt();
        byte[] bytes = new byte[len];
        dataInput.readFully(bytes);
        return bytes;
    }
}
