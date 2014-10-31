package com.lamfire.chimaera.store.leveldbstore;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lamfire.utils.*;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.*;

import com.lamfire.logger.Logger;

public class LDBManager {
	private static final Logger LOGGER = Logger.getLogger(LDBManager.class);
    public static final String META_KEY_PREFIX_DATABASE = "[DATABASE]";
    public static final WriteOptions WRITE_SYNC = new WriteOptions();

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final Lock lock = new ReentrantLock();
	private final Map<String, DB> dbs = Maps.newHashMap(); //dbs
    private final Map<String, LDBDatabase> databases = Maps.newHashMap(); //dbs
    private final Map<String,Long> lastAccessTimeRecorder = Maps.newHashMap();

    private final DBFactory factory ;
	private final String rootDir;
    private final Options options;

	public LDBManager(String rootDir) {
		this.rootDir = rootDir;
        this.options = new Options();
        this.options.createIfMissing(true);
        this.factory = new JniDBFactory();
        Threads.scheduleWithFixedDelay(new AutoCloseIdleDatabaseTask(),5,5, TimeUnit.MINUTES);
	}

    public LDBManager(String rootDir, Options options) {
        this.rootDir = rootDir;
        this.options = options;
        this.factory = new JniDBFactory();
        Threads.scheduleWithFixedDelay(new AutoCloseIdleDatabaseTask(),5,5, TimeUnit.MINUTES);
    }

    public String getDatabaseDir(String name){
        return FilenameUtils.concat(rootDir,name);
    }
	
	public static byte[] asBytes(String text) {
		return text.getBytes(DEFAULT_CHARSET);
	}

	public static String asString(byte[] bytes) {
		return new String(bytes,DEFAULT_CHARSET);
	}

    DB _db(String name){
        lock.lock();
        try{
            lastAccessTimeRecorder.put(name,System.currentTimeMillis());
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }
            return _makeDB(name, options);
        }finally {
            lock.unlock();
        }
	}

    public LDBDatabase getDB(String name){
        lock.lock();
        try{
            LDBDatabase database = databases.get(name);
            if(database == null){
                database = new LDBDatabase(this,name);
                databases.put(name,database);
            }
            return database;
        }finally {
            lock.unlock();
        }
    }
	
	private DB _makeDB(String name,Options options){
        lock.lock();
        try{
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }
            try {
                String databaseDir = getDatabaseDir(name);
                if(!FileUtils.exists(databaseDir)){
                    LOGGER.info("Path not found,make dir - " + databaseDir);
                    FileUtils.makeDirs(databaseDir);
                }
                db =  factory.open(new File(databaseDir), options);
                dbs.put(name, db);
                LOGGER.info("[OPEN]:" + databaseDir);
                return db;
            } catch (IOException e) {
                LOGGER.error(e);
                throw new RuntimeException(e);
            }
        }finally {
            lock.unlock();
        }
	}

    public void deleteDB(String name){
        lock.lock();
        try{
            DB db = dbs.remove(name);
            if(db != null){
                try {
                    db.close();
                    databases.remove(name);
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
            String databaseDir = getDatabaseDir(name);
            try {
                FileUtils.deleteDirectory(new File(databaseDir));
                LOGGER.info("[DELETE]:" + databaseDir);
            } catch (IOException e) {
                LOGGER.warn(e);
            }
        }finally {
            lock.unlock();
        }
    }

    public  void closeDB(String name){
        lock.lock();
        try{
            DB db = dbs.remove(name);
            if(db != null){
                try {
                    db.close();
                    databases.remove(name);
                    LOGGER.debug("[CLOSED]:" + name);
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
        }finally {
            lock.unlock();
        }
    }

    public void closeAll(){
        lock.lock();
        try{
            databases.clear();
            for(DB db : dbs.values()){
                try {
                    db.close();
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
            dbs.clear();
        }finally {
            lock.unlock();
        }
    }

    public static void closeIterator(DBIterator it){
        try {
            if(it != null)it.close();
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }

    static{
        WRITE_SYNC.sync(true);
    }


    class AutoCloseIdleDatabaseTask implements Runnable{
        @Override
        public void run() {

            for(Map.Entry<String,Long> e :lastAccessTimeRecorder.entrySet() ){
                String name = e.getKey();
                long accessTime = e.getValue();
                LOGGER.debug("[IDLE_CHECK]:" + name + " last access time " + DateFormatUtils.format(accessTime,"yyyy-MM-dd hh:mm:ss"));
                executeIdle(name,accessTime);
            }
        }

        private void executeIdle(String name,long lastAccessTime){
            try{
                if((System.currentTimeMillis() - lastAccessTime) > DateUtils.MILLIS_PER_MINUTE * 5){
                    lastAccessTimeRecorder.remove(name);
                    closeDB(name);
                    LOGGER.info("[CLOSED]:the db["+name+"] exceeded the allowed maximum idle time.");
                }
            }catch (Throwable t){
                LOGGER.warn(t);
            }
        }
    }
}
