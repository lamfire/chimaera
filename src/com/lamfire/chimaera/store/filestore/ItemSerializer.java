package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.Item;
import com.lamfire.thalia.serializer.Serializer;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-24
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class ItemSerializer implements Serializer<Item>, Serializable {

    @Override
    public void serialize(DataOutput dataOutput, Item item) throws IOException {
        dataOutput.writeLong(item.getValue());
        dataOutput.writeUTF(item.getName());
    }

    @Override
    public Item deserialize(DataInput dataInput) throws IOException, ClassNotFoundException {
        Item item = new Item();
        item.setValue(dataInput.readLong());
        item.setName(dataInput.readUTF());
        return item;
    }
}
