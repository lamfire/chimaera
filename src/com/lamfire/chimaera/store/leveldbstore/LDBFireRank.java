package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.code.MurmurHash;
import com.lamfire.code.UUIDGen;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.Lists;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-26
 * Time: 上午9:22
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireRank implements FireRank {
    private final Lock lock = new ReentrantLock();
    private LevelDB levelDB;
    private DB db;
    private DB indexDB;
    private byte[] sizeKey;
    private String name;
    private String indexName;
    private int seed = 11;

    public LDBFireRank(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        this.indexName = name + "_idx";
        init();
    }

    private void init() {
        this.db = levelDB.getDB(name);
        this.indexDB = levelDB.getDB(indexName);
        this.sizeKey = levelDB.encodeSizeKey(name);
        this.seed = MurmurHash.hash32(levelDB.bytes(name), seed);
    }

    private void incrSize() {
        levelDB.incrementMeta(this.sizeKey);
    }

    private void decrSize() {
        levelDB.incrementMeta(this.sizeKey, -1);
    }

    public byte[] encodeScoreKey(String scoreKey, long score) {
        long time = UUIDGen.getTimeSafe();
        long hash = MurmurHash.hash64(levelDB.bytes(scoreKey), seed);

        byte[] scoreBytes = Bytes.toBytes(score);
        byte[] timeBytes = Bytes.toBytes(time);
        byte[] hashBytes = Bytes.toBytes(hash);

        byte[] result = ArrayUtils.addAll(scoreBytes, timeBytes);
        result = ArrayUtils.addAll(result, hashBytes);
        return result;
    }

    public long decodeScore(byte[] scoreKey) {
        return Bytes.toLong(scoreKey, 0);
    }

    public byte[] encodeIndexKey(String scoreKey) {
        return levelDB.bytes(scoreKey);
    }

    private long getScore(byte[] scoreKey) {
        long score = 0;
        byte[] bytes = db.get(scoreKey);
        if (bytes != null) {
            score = Bytes.toLong(bytes);
        }
        return score;
    }

    private void update(byte[] nameKey, byte[] oldScoreKey, byte[] newScoreKey, long score) {
        indexDB.put(nameKey, newScoreKey);
        if (oldScoreKey != null) {
            db.delete(oldScoreKey);
        }
        db.put(newScoreKey, nameKey);
    }

    @Override
    public void put(String name) {
        long score = 0;
        byte[] nameKey = levelDB.bytes(name);
        byte[] scoreKey = indexDB.get(nameKey);
        if (scoreKey == null) {
            score = 1;
            scoreKey = encodeScoreKey(name, score);
            update(nameKey, null, scoreKey, score);
            incrSize();
            return;
        }

        score = decodeScore(scoreKey);
        score++;
        byte[] newScoreKey = encodeScoreKey(name, score);
        update(nameKey, scoreKey, newScoreKey, score);
    }

    @Override
    public void incr(String name, long step) {
        long score = 0;
        byte[] nameKey = levelDB.bytes(name);
        byte[] scoreKey = indexDB.get(nameKey);
        if (scoreKey == null) {
            score = step;
            scoreKey = encodeScoreKey(name, score);
            update(nameKey, null, scoreKey, score);
            incrSize();
            return;
        }

        score = decodeScore(scoreKey);
        score += step;
        byte[] newScoreKey = encodeScoreKey(name, score);
        update(nameKey, scoreKey, newScoreKey, score);
    }

    @Override
    public void set(String name, long score) {
        byte[] nameKey = levelDB.bytes(name);
        byte[] scoreKey = indexDB.get(nameKey);
        if (scoreKey == null) {
            scoreKey = encodeScoreKey(name, score);
            update(nameKey, null, scoreKey, score);
            incrSize();
            return;
        }

        score = decodeScore(scoreKey);
        byte[] newScoreKey = encodeScoreKey(name, score);
        update(nameKey, scoreKey, newScoreKey, score);
    }

    @Override
    public long score(String name) {
        byte[] nameKey = levelDB.bytes(name);
        byte[] scoreKey = indexDB.get(nameKey);
        if (scoreKey != null) {
            return decodeScore(scoreKey);
        }
        return 0;
    }

    @Override
    public long remove(String name) {
        long score = 0;
        byte[] nameKey = levelDB.bytes(name);
        byte[] scoreKey = indexDB.get(nameKey);
        if (scoreKey != null) {
            score = decodeScore(scoreKey);
            indexDB.delete(nameKey);
            db.delete(scoreKey);
            decrSize();
        }

        return score;
    }

    @Override
    public List<Item> max(int size) {
        List<Item> result = Lists.newArrayList();
        DBIterator it = db.iterator();
        it.seekToLast();

        Map.Entry<byte[], byte[]> e = it.peekNext();
        Item item = new Item();
        item.setName(levelDB.asString(e.getValue()));
        item.setValue(decodeScore(e.getKey()));
        result.add(item);

        while (it.hasPrev()) {
            e = it.prev();
            item = new Item();
            item.setName(levelDB.asString(e.getValue()));
            long score = decodeScore(e.getKey());
            item.setValue(score);
            result.add(item);
            if (result.size() >= size) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Item> min(int size) {
        List<Item> result = Lists.newArrayList();
        DBIterator it = db.iterator();
        it.seekToFirst();
        while (it.hasNext()) {
            Map.Entry<byte[], byte[]> e = it.next();
            Item item = new Item();
            item.setName(levelDB.asString(e.getValue()));
            long score = decodeScore(e.getKey());
            item.setValue(score);
            result.add(item);
            if (result.size() >= size) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Item> maxRange(int from, int size) {
        List<Item> result = Lists.newArrayList();
        DBIterator it = db.iterator();
        it.seekToLast();

        //add last;
        if (from == 0) {
            Map.Entry<byte[], byte[]> e = it.next();
            Item item = new Item();
            item.setName(levelDB.asString(e.getValue()));
            long score = decodeScore(e.getKey());
            item.setValue(score);
            result.add(item);
        }

        it.seekToLast();
        //skip
        for(int i=1;i<from;i++){
            if(it.hasPrev())it.prev();
        }


        //
        while (it.hasPrev()) {
            Map.Entry<byte[], byte[]> e = it.prev();
            Item item = new Item();
            item.setName(levelDB.asString(e.getValue()));
            long score = decodeScore(e.getKey());
            item.setValue(score);
            result.add(item);
            if (result.size() >= size) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Item> minRange(int from, int size) {
        List<Item> result = Lists.newArrayList();
        DBIterator it = db.iterator();
        it.seekToFirst();
        //skip
        int i = 0;
        while (it.hasPrev()) {
            if (i >= from) {
                break;
            }
            i++;
        }

        //
        while (it.hasNext()) {
            Map.Entry<byte[], byte[]> e = it.next();
            Item item = new Item();
            item.setName(levelDB.asString(e.getValue()));
            long score = decodeScore(e.getKey());
            item.setValue(score);
            result.add(item);
            if (result.size() >= size) {
                break;
            }
        }
        return result;
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
            levelDB.deleteDB(indexName);
            levelDB.removeMeta(sizeKey);
            init();
        } finally {
            lock.unlock();
        }
    }
}
