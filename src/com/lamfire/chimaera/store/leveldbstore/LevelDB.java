package com.lamfire.chimaera.store.leveldbstore;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.FilenameUtils;
import com.lamfire.utils.Maps;

public class LevelDB {
	private static final Logger LOGGER = Logger.getLogger(LevelDB.class);
    private static final String META_NAME = ".meta";
	private Charset charset = Charset.forName("UTF-8");
	private DBFactory factory = new JniDBFactory();
    private final Lock lock = new ReentrantLock();
	private Map<String, DB> dbs = Maps.newHashMap(); //dbs
	private DB metaDb; //size db
	private String rootDir;

	public LevelDB(String rootDir) {
		this.rootDir = rootDir;
	}

	public void open() {
        lock.lock();
        try{
            if(metaDb != null){
                throw new RuntimeException("already opened : " + rootDir);
            }
            // make data dir
            if (!FileUtils.exists(rootDir)) {
                FileUtils.makeDirs(rootDir);
            }

            //open meta db
            Options options = new Options();
            options.createIfMissing(true);
            options.cacheSize(1024 * 1024);
            try {
                metaDb = factory.open(new File(getDBDir(META_NAME)), options);
            } catch (IOException e) {
                LOGGER.error(e);
                throw new RuntimeException(e);
            }
        }finally {
            lock.unlock();
        }
	}

    public String getDBDir(String name){
        return FilenameUtils.concat(rootDir,name);
    }
	
	public byte[] bytes(String text) {
		return text.getBytes(charset);
	}

	public String asString(byte[] bytes) {
		return new String(bytes,charset);
	}
	
	byte[] encodeSizeKey(String name){
		byte [] sizeKey = (name+"_SIZE").getBytes(charset);
		return sizeKey;
	}

    byte[] encodeWriteIndexKey(String name){
        byte [] sizeKey = (name+"_WRITE_INDEX").getBytes(charset);
        return sizeKey;
    }

    byte[] encodeReadIndexKey(String name){
        byte [] sizeKey = (name+"_READ_INDEX").getBytes(charset);
        return sizeKey;
    }

    long incrementMeta(byte[] key){
        return incrementMeta(key,1);
    }

    long incrementMeta(byte[] key,long step){
        lock.lock();
        try{
        long value = getMetaValueAsLong(key);
        value += step;
        metaDb.put(key, Bytes.toBytes(value));
        return value;
        }finally {
            lock.unlock();
        }
    }

    long getMetaValueAsLong(byte[] key){
        lock.lock();
        try{
            long value = 0;
            byte[] bytes = metaDb.get(key);
            if(bytes != null){
                value = Bytes.toLong(bytes);
            }
            return value;
        }finally {
            lock.unlock();
        }
    }

    void removeMeta(byte[] key){
        lock.lock();
        try{
            if(metaDb.get(key) != null){
                metaDb.delete(key);
            }
        }finally {
            lock.unlock();
        }
    }

	public synchronized DB getDB(String name){
        lock.lock();
        try{
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }

            Options options = new Options();
            options.createIfMissing(true);
            return getDB(name,options);
        }finally {
            lock.unlock();
        }
	}
	
	public synchronized DB getDB(String name,Options options){
        lock.lock();
        try{
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }
            try {
                db =  factory.open(new File(getDBDir(name)), options);
                dbs.put(name, db);
                return db;
            } catch (IOException e) {
                LOGGER.error(e);
                throw new RuntimeException(e);
            }
        }finally {
            lock.unlock();
        }
	}

    public synchronized void deleteDB(String name){
        lock.lock();
        try{
            DB db = dbs.remove(name);
            if(db != null){
                try {
                    db.close();
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
            String dbDir = getDBDir(name);
            try {
                FileUtils.deleteDirectory(new File(dbDir));
            } catch (IOException e) {
                LOGGER.warn(e);
            }
        }finally {
            lock.unlock();
        }
    }

    public void close(){
        lock.lock();
        try{
            for(DB db : dbs.values()){
                try {
                    db.close();
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
            dbs.clear();

            try {
                metaDb.close();
            } catch (IOException e) {
                LOGGER.warn(e);
            }
            metaDb = null;
        }finally {
            lock.unlock();
        }
    }

}
