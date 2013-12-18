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
public class StringSerializer implements Serializer<String> {

    @Override
    public void serialize(SerializerOutput serializerOutput, String value) throws IOException {
        serializerOutput.writeUTF(value);
    }

    @Override
    public String deserialize(SerializerInput serializerInput) throws IOException, ClassNotFoundException {
        return serializerInput.readUTF();
    }
}
