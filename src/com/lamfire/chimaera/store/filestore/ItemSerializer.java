package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.Item;
import com.lamfire.json.JSON;
import org.apache.jdbm.Serializer;

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
public class ItemSerializer implements Serializer<Item> ,Serializable {

    @Override
    public void serialize(DataOutput dataOutput,Item item) throws IOException {
        byte[] bytes = JSON.toJSONString(item).getBytes();
        dataOutput.writeInt(bytes.length);
        dataOutput.write(bytes);
    }

    @Override
    public Item deserialize(DataInput dataInput) throws IOException, ClassNotFoundException {
        int len = dataInput.readInt();
        byte[] bytes = new byte[len];
        dataInput.readFully(bytes);
        String js = new String(bytes);
        JSON json = new JSON(js);
        return json.toObject(Item.class);
    }
}
