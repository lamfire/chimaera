package com.lamfire.chimaera.store;

import java.util.List;

public interface FireList extends FireCollection {

    public boolean add(byte[] value);

    public void set(int index, byte[] value);

    public byte[] get(int index);

    public List<byte[]> gets(int fromIndex, int size);

    public byte[] remove(int index);

}
