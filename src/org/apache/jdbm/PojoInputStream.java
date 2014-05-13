package org.apache.jdbm;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.util.ArrayList;

/**
 * An alternative to <code>java.io.ObjectInputStream</code> which uses more efficient serialization
 */
public class PojoInputStream extends DataInputStream implements ObjectInput {


    public PojoInputStream(InputStream in) {
        super(in);
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        //first read class data
        ArrayList<SerialClassInfo.ClassInfo> info = SerialClassInfo.serializer.deserialize(this);

        Serialization ser = new Serialization(null,0,info);
        return ser.deserialize(this);
    }
}
