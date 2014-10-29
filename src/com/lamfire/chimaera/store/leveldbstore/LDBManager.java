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
    /**
     * write_buffer调大，可以让写的次数降低，写的强度提高.
     * 写缓冲大小，增加会提高写的性能，但是会增加启动的时间，因为有更多的数据需要恢复.
     */
    private static final int OPTIONS_WRITE_BUFFER_SIZE = 8 * 1024 * 1024;
    private static final int OPTIONS_CACHE_SIZE = 8 * 1024 * 1024;
    private static final int OPTIONS_MAX_OPEN_FILES = 1024;
    /**
     * block默认大小为4k，由LevelDB调用open函数时传入的options.block_size参数指定；LevelDB的代码中限制的block最小大小为1k，最大大小为4M。对于频繁做scan操作的应用，可适当调大此参数，对大量小value随机读取的应用，也可尝试调小该参数；
     */
    private static final int OPTIONS_BLOCK_SIZE = 1024 * 1024;
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final Lock lock = new ReentrantLock();
	private final Map<String, DB> dbs = Maps.newHashMap(); //dbs
    private final Map<String,Long> lastAccessTimeRecorder = Maps.newHashMap();

    private final DBFactory factory ;
	private final String rootDir;
    private final Options options;

	public LDBManager(String rootDir) {
		this.rootDir = rootDir;
        this.options = new Options();
        this.options.createIfMissing(true);
        this.options.writeBufferSize(OPTIONS_WRITE_BUFFER_SIZE);
        this.options.cacheSize(OPTIONS_CACHE_SIZE);
        this.options.maxOpenFiles(OPTIONS_MAX_OPEN_FILES);
        this.options.blockSize(OPTIONS_BLOCK_SIZE);
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

	public synchronized DB borrowDB(String name){
        lock.lock();
        try{
            lastAccessTimeRecorder.put(name,System.currentTimeMillis());
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }
            return makeDB(name, options);
        }finally {
            lock.unlock();
        }
	}
	
	private synchronized DB makeDB(String name,Options options){
        lock.lock();
        try{
            DB db = dbs.get(name);
            if(db != null ){
                return db;
            }
            try {
                String databaseDir = getDatabaseDir(name);
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

    public synchronized void closeDB(String name){
        lock.lock();
        try{
            DB db = dbs.remove(name);
            if(db != null){
                try {
                    db.close();
                    LOGGER.info("[CLOSED]:" + name);
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
