package com.lamfire.thalia;

import java.io.IOException;

/**
 * An listener notifed when record is inserted, updated or removed.
 */
public interface EntryListener<K, V> {

    void inserted(K key, V value) throws IOException;

    void updated(K key, V oldValue, V newValue) throws IOException;

    void removed(K key, V value) throws IOException;

}
