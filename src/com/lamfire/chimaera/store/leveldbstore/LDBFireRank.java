package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.code.MurmurHash;
import com.lamfire.code.UUIDGen;
import com.lamfire.logger.Logger;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.Lists;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.WriteOptions;

import java.io.IOException;
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
    private static final Logger LOGGER = Logger.getLogger(LDBFireRank.class);
    private final Lock lock = new ReentrantLock();
    private LevelDB levelDB;
    private DB _db;
    private DB _indexDB;
    private byte[] sizeKey;
    private String name;
    private String indexName;
    private int seed = 11;

    public LDBFireRank(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        this.indexName = name + "_idx";
        this.sizeKey = levelDB.encodeSizeKey(name);
        this.seed = MurmurHash.hash32(levelDB.asBytes(name), seed);
    }


    private synchronized DB getDB() {
        if (this._db == null) {
            _db = levelDB.getDB(name);
        }
        return _db;
    }

    private synchronized DB getIndexDB() {
        if (this._indexDB == null) {
            _indexDB = levelDB.getDB(indexName);
        }
        return _indexDB;
    }

    private void incrSize() {
        levelDB.incrementMeta(this.sizeKey);
    }

    private void decrSize() {
        levelDB.incrementMeta(this.sizeKey, -1);
    }

    public byte[] encodeScoreKey(String scoreKey, long score) {
        long time = UUIDGen.getTimeSafe();
        long hash = MurmurHash.hash64(levelDB.asBytes(scoreKey), seed);

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
        return levelDB.asBytes(scoreKey);
    }

    private long getScore(byte[] scoreKey) {
        try {
            lock.lock();
            long score = 0;
            byte[] bytes = getDB().get(scoreKey);
            if (bytes != null) {
                score = Bytes.toLong(bytes);
            }
            return score;
        } finally {
            lock.unlock();
        }
    }

    private void update(byte[] nameKey, byte[] oldScoreKey, byte[] newScoreKey, long score) {
        try {
            lock.lock();
            getIndexDB().put(nameKey, newScoreKey);
            if (oldScoreKey != null) {
                getDB().delete(oldScoreKey);
            }
            getDB().put(newScoreKey, nameKey);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(String name) {
        try {
            lock.lock();
            long score = 0;
            byte[] nameKey = levelDB.asBytes(name);
            byte[] scoreKey = getIndexDB().get(nameKey);
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
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void incr(String name, long step) {
        try {
            lock.lock();
            long score = 0;
            byte[] nameKey = levelDB.asBytes(name);
            byte[] scoreKey = getIndexDB().get(nameKey);
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
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(String name, long score) {
        try {
            lock.lock();
            byte[] nameKey = levelDB.asBytes(name);
            byte[] scoreKey = getIndexDB().get(nameKey);
            if (scoreKey == null) {
                scoreKey = encodeScoreKey(name, score);
                update(nameKey, null, scoreKey, score);
                incrSize();
                return;
            }

            score = decodeScore(scoreKey);
            byte[] newScoreKey = encodeScoreKey(name, score);
            update(nameKey, scoreKey, newScoreKey, score);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long score(String name) {
        try {
            lock.lock();
            byte[] nameKey = levelDB.asBytes(name);
            byte[] scoreKey = getIndexDB().get(nameKey);
            if (scoreKey != null) {
                return decodeScore(scoreKey);
            }
            return 0;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long remove(String name) {
        try {
            lock.lock();
            long score = 0;
            byte[] nameKey = levelDB.asBytes(name);
            byte[] scoreKey = getIndexDB().get(nameKey);
            if (scoreKey != null) {
                score = decodeScore(scoreKey);
                getIndexDB().delete(nameKey);
                getDB().delete(scoreKey);
                decrSize();
            }

            return score;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> max(int size) {
        List<Item> result = Lists.newArrayList();
        DBIterator it = getDB().iterator();
        try {
            lock.lock();
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
        }finally {
            lock.unlock();
            LevelDB.closeIterator(it);
        }
    }

    @Override
    public List<Item> min(int size) {
        DBIterator it = getDB().iterator();
        try {
            lock.lock();
            List<Item> result = Lists.newArrayList();
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
        } finally {
            lock.unlock();
            LevelDB.closeIterator(it);
        }
    }

    @Override
    public List<Item> maxRange(int from, int size) {
        DBIterator it = getDB().iterator();
        try {
            lock.lock();
            List<Item> result = Lists.newArrayList();

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
            for (int i = 1; i < from; i++) {
                if (it.hasPrev()) it.prev();
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
        } finally {
            lock.unlock();
            LevelDB.closeIterator(it);
        }
    }

    @Override
    public List<Item> minRange(int from, int size) {
        DBIterator it = getDB().iterator();
        try {
            lock.lock();
            List<Item> result = Lists.newArrayList();

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
        } finally {
            lock.unlock();
            LevelDB.closeIterator(it);
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
            levelDB.deleteDB(indexName);
            levelDB.removeMeta(sizeKey);
            this._db = null;
            this._indexDB = null;
        } finally {
            lock.unlock();
        }
    }
}
