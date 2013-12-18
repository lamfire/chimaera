package com.lamfire.chimaera.store.filestore.serializer;

import jdbm.Serializer;
import jdbm.SerializerInput;
import jdbm.SerializerOutput;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
public class BytesSerializer implements Serializer<byte[]> {

    @Override
    public void serialize(SerializerOutput serializerOutput, byte[] bytes) throws IOException {
        serializerOutput.writeInt(bytes.length);
        serializerOutput.write(bytes);
    }

    @Override
    public byte[] deserialize(SerializerInput serializerInput) throws IOException, ClassNotFoundException {
        int len = serializerInput.readInt();
        byte[] bytes = new byte[len];
        serializerInput.readFully(bytes);
        return bytes;
    }
}
