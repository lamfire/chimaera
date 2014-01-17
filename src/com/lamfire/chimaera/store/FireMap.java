package com.lamfire.chimaera.store;

import java.util.List;

public interface FireMap extends FireCollection {

    public void put(String key, byte[] value);

    public List<String> keys();

    public byte[] get(String key);

    public void remove(String key);

    public boolean exists(String key);

}
