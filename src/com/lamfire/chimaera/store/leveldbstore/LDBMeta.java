package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.Maps;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-10-29
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 */
public class LDBMeta extends LDBDatabase{
    private static final Logger LOGGER = Logger.getLogger(LDBMeta.class);
    private static final String META_NAME = ".meta";
    public static final String META_KEY_PREFIX_DATABASE = "[DATABASE]";
    public static final String META_KEY_PREFIX_SIZE = "[SIZE]";
    public static final String META_KEY_PREFIX_WRITE_INDEX = "[WRITE_INDEX]";
    public static final String META_KEY_PREFIX_READ_INDEX = "[READ_INDEX]";

    private final Charset charset = Charset.forName("UTF-8");

    private Lock lock = new ReentrantLock();

    public LDBMeta(LDBManager manager){
        super(manager,META_NAME);
    }

    public byte[] getSizeKey(String name){
        byte [] sizeKey = (META_KEY_PREFIX_SIZE +":"+name).getBytes(charset);
        return sizeKey;
    }

    public byte[] getWriteIndexKey(String name){
        byte [] sizeKey = (META_KEY_PREFIX_WRITE_INDEX+ ":"+name).getBytes(charset);
        return sizeKey;
    }

    public byte[] getReadIndexKey(String name){
        byte [] sizeKey = (META_KEY_PREFIX_READ_INDEX+":"+name).getBytes(charset);
        return sizeKey;
    }

    public byte[] getDatabaseKeyByName(String name){
        byte [] sizeKey = (META_KEY_PREFIX_DATABASE +":"+name).getBytes(charset);
        return sizeKey;
    }

    public String getDatabaseNameByKey(byte[] key){
        String keyStr = new String(key,charset);
        return keyStr.substring(META_KEY_PREFIX_DATABASE.length() +1);
    }

    public long increment(byte[] key){
        return increment(key, 1);
    }

    public long increment(byte[] key,long step){
        lock.lock();
        try{
            long value = getValueAsLong(key);
            value += step;
            put(key, Bytes.toBytes(value));
            return value;
        }finally {
            lock.unlock();
        }
    }

    public byte[] getValue(byte[] key){
        lock.lock();
        try{
            byte[] bytes = get(key);
            return bytes;
        }finally {
            lock.unlock();
        }
    }

    public void setValue(byte[] key,byte[] value){
        lock.lock();
        try{
            put(key,value);
        }finally {
            lock.unlock();
        }
    }

    public long getValueAsLong(byte[] key){
        lock.lock();
        try{
            long value = 0;
            byte[] bytes = get(key);
            if(bytes != null){
                value = Bytes.toLong(bytes);
            }
            return value;
        }finally {
            lock.unlock();
        }
    }

    public String getValueAsString(byte[] key){
        lock.lock();
        try{
            String value = null;
            byte[] bytes = get(key);
            if(bytes != null){
                value = Bytes.toString(bytes);
            }
            return value;
        }finally {
            lock.unlock();
        }
    }

    public void remove(byte[] key){
        lock.lock();
        try{
            if(get(key) != null){
                super.delete(key);
            }
        }finally {
            lock.unlock();
        }
    }

    public synchronized Map<byte[],byte[]> findByPrefix(final byte[] prefix){
        Map<byte[],byte[]> map = Maps.newHashMap();
        DBIterator it = iterator();
        try{
            it.seekToFirst();
            while(it.hasNext()){
                Map.Entry<byte[],byte[]> entry = it.next();
                byte[] key = entry.getKey();
                byte[] head = Bytes.head(key,prefix.length);
                if(Bytes.equals(prefix,head)){
                    map.put(key,entry.getValue());
                }
            }
            return map;
        }finally {
            LDBManager.closeIterator(it);
        }
    }

    public synchronized void clear(){
        DBIterator it = iterator();
        try{
            it.seekToFirst();
            while(it.hasNext()){
                Map.Entry<byte[],byte[]> entry = it.next();
                byte[] key = entry.getKey();
                delete(key);
            }
        }finally {
            LDBManager.closeIterator(it);
        }
    }

    public void close(){
        try {
            super.close();
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }
}
