package com.lamfire.chimaera.store;

import java.util.List;

public interface FireSet extends FireCollection {

    public void add(byte[] value);

    public byte[] remove(int index);

    public byte[] remove(byte[] value);

    public byte[] get(int index);

    public List<byte[]> gets(int fromIndex, int size);

    public boolean exists(byte[] bytes);

}
