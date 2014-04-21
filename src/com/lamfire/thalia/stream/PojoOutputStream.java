package com.lamfire.thalia.stream;

import java.io.*;
import java.util.ArrayList;

import com.lamfire.thalia.serializer.SerialClassInfo;
import com.lamfire.thalia.serializer.Serialization;

/**
 * An alternative to <code>java.io.ObjectOutputStream</code> which uses more efficient serialization
 */
public class PojoOutputStream extends DataOutputStream implements ObjectOutput {

    public PojoOutputStream(OutputStream out) {
        super(out);
    }

    public void writeObject(Object obj) throws IOException {
        ArrayList registered = new ArrayList();
        Serialization ser = new Serialization(null,0,registered);

        byte[] data = ser.serialize(obj);
        //write class info first
        SerialClassInfo.serializer.serialize(this, registered);
        //and write data
        write(data);
    }
}
