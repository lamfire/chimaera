package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.code.MurmurHash;
import com.lamfire.hydra.exception.NotSupportedMethodException;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.Lists;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireSet implements FireSet {
    private final Lock lock = new ReentrantLock();
    private int hashSeed = 13;
    private LevelDB levelDB;
    private DB _db;
    private byte[] sizeKey;
    private String name;

    public LDBFireSet(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        _db = levelDB.getDB(name);
        this.sizeKey = levelDB.encodeSizeKey(name);
        this.hashSeed = MurmurHash.hash32(levelDB.asBytes(name), 11);
    }

    private synchronized DB getDB(){
        if(this._db == null){
            _db = levelDB.getDB(name);
        }
        return _db;
    }

    private long hash(byte[] bytes) {
        return MurmurHash.hash64(bytes, hashSeed);
    }

    private void incrSize() {
        levelDB.incrementMeta(this.sizeKey);
    }

    private void decrSize() {
        levelDB.incrementMeta(this.sizeKey, -1);
    }

    private byte[] makeHashKey(byte[] bytes) {
        long hash = hash(bytes);
        return Bytes.toBytes(hash);
    }

    @Override
    public void add(byte[] value) {
        try {
            lock.lock();
            byte[] key = makeHashKey(value);
            byte[] oldValue = getDB().get(key);
            if (oldValue != null) {
                return;
            }
            getDB().put(key, value);
            incrSize();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] remove(int index) {
        throw new NotSupportedMethodException("public byte[] remove(int index)");
    }

    @Override
    public byte[] remove(byte[] value) {
        try {
            lock.lock();
            byte[] key = makeHashKey(value);
            byte[] oldBytes = getDB().get(key);
            if (oldBytes != null) {
                getDB().delete(key);
                decrSize();
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(int index) {
        if(index < 0){
            throw new IndexOutOfBoundsException("Index " + index +",Size " + size());
        }

        DBIterator it = getDB().iterator();
        try {
            lock.lock();

            it.seekToFirst();
            int i = 0;
            byte[] result = null;
            while(it.hasNext()){
                byte[] bytes = it.next().getValue();
                if(i == index){
                    result = bytes;
                    break;
                }
                i++;
            }
            if(i == index){
                return  result;
            }
            throw new IndexOutOfBoundsException("Index " + index +",Size " + size());
        } finally {
            lock.unlock();
            LevelDB.closeIterator(it);
        }
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        DBIterator it = getDB().iterator();
        try {
            lock.lock();
            List<byte[]> list = Lists.newArrayList();

            it.seekToFirst();
            int i = 0;
            while(it.hasNext()){
                byte[] bytes = it.next().getValue();
                if(i >= fromIndex){
                    list.add(bytes);
                    if(list.size() == size){
                        break;
                    }
                }
                i++;
            }
            return list;
        } finally {
            lock.unlock();
            LevelDB.closeIterator(it);
        }
    }

    @Override
    public boolean exists(byte[] bytes) {
        try {
            lock.lock();
            byte[] key = makeHashKey(bytes);
            byte[] oldBytes = getDB().get(key);
            if (oldBytes != null) {
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        try {
            lock.lock();
            return levelDB.getMetaValueAsLong(this.sizeKey);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(sizeKey);
            this._db = null;
        } finally {
            lock.unlock();
        }
    }
}
