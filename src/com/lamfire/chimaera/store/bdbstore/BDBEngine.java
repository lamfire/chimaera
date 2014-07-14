package com.lamfire.chimaera.store.bdbstore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.logger.Logger;
import com.sleepycat.bind.ByteArrayBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredContainer;
import com.sleepycat.collections.StoredKeySet;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredSortedKeySet;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;

@SuppressWarnings("unchecked")
public class BDBEngine {

	private static final Logger logger = Logger.getLogger(BDBEngine.class);
    private static final String META_SEQUENCE_TABLE_NAME = " META_SEQUENCE_TABLE";
	private final Map<String,Database> openedDatabases = new HashMap<String, Database>();
	private final Map<String,StoredContainer> containers = new HashMap<String, StoredContainer>();
	
	
	private String name;
	private BDBOpts config;
	private Environment environment;

    private final BDBFireIncrement metaSequenceTable;

	public BDBEngine(String name, BDBOpts config) throws BDBStorageException {
		this.name = name;
		this.config = config;
		initEnvironment();
        metaSequenceTable = new BDBFireIncrement(this,META_SEQUENCE_TABLE_NAME);
	}

	private void initEnvironment() throws BDBStorageException {

		try {
			EnvironmentConfig environmentConfig = new EnvironmentConfig();
			environmentConfig.setTransactional(config.isWriteTransactionsEnabled());
			environmentConfig.setCacheSize(config.getCacheSize());
			if (config.isWriteTransactionsEnabled() && config.isFlushTransactionsEnabled()) {
				environmentConfig.setDurability(Durability.COMMIT_SYNC);
			} else if (config.isWriteTransactionsEnabled() && (!config.isFlushTransactionsEnabled())) {
				environmentConfig.setDurability(Durability.COMMIT_WRITE_NO_SYNC);
			} else {
				environmentConfig.setDurability(Durability.COMMIT_NO_SYNC);
			}
			environmentConfig.setAllowCreate(true);
			environmentConfig.setConfigParam(EnvironmentConfig.LOG_FILE_MAX, Long.toString(config.getMaxLogFileSize()));
			environmentConfig.setConfigParam(EnvironmentConfig.CHECKPOINTER_BYTES_INTERVAL, Long.toString(config.getCheckpointBytes()));
			environmentConfig.setConfigParam(EnvironmentConfig.CHECKPOINTER_WAKEUP_INTERVAL, Long.toString(config.getCheckpointMs() * BDBOpts.US_PER_MS));


			File bdbDir = new File(config.getPath(), name);
			if (!bdbDir.exists()) {
				logger.debug("Creating database directory: " + bdbDir.getAbsolutePath());
				if (!bdbDir.mkdirs()) {
					throw new BDBStorageException("Failed to create database directory: " + bdbDir.getAbsolutePath());
				}
			}

			environment = new Environment(bdbDir, environmentConfig);
		} catch (DatabaseException e) {
			throw new BDBStorageException("Failed to open database", e);
		}
	}
	
	
	public DatabaseConfig makeDatabaseConfig(){
		DatabaseConfig conf = new DatabaseConfig();
		conf.setAllowCreate(true);
		conf.setSortedDuplicates(false);
		conf.setNodeMaxEntries(config.getBtreeFanout());
		conf.setTransactional(environment.getConfig().getTransactional());
		return conf;
	}

	public void close() throws BDBStorageException {
		logger.debug("closing database: " + name);
		try {
			closeAllDatabase();
			environment.sync();
			environment.close();
		} catch (DatabaseException e) {
			if (environment != null) {
				environment.close();
			}
			throw new BDBStorageException("Failed to close database: " + name, e);
		} finally {
			environment = null;
			containers.clear();
			openedDatabases.clear();
		}
	}
	
	private void closeAllDatabase()throws BDBStorageException {
		for(Database db : openedDatabases.values()){
			db.close();
		}
		openedDatabases.clear();
		containers.clear();
	}

	public boolean isClosed() throws BDBStorageException {
		boolean l = false;
		try {
			if (environment != null && !openedDatabases.isEmpty()) {
				return l;
			} else {
				l = true;
				if (environment != null) {
					environment.sync();
					environment.close();
				}
				if (!openedDatabases.isEmpty()) {
					openedDatabases.clear();
				}
			}
			return l;
		} catch (DatabaseException e) {
			l = true;
			if (environment != null) {
				environment.close();
			}
			if (!openedDatabases.isEmpty()) {
				openedDatabases.clear();
			}
			return l;
		} finally {
			if (l) {
				environment = null;
				if (!openedDatabases.isEmpty()) {
					openedDatabases.clear();
				}
			}

		}
	}

	public List<String> getCollectionNames() throws BDBStorageException {
		return environment.getDatabaseNames();
	}

    public boolean exists(String name){
        try{
            return getCollectionNames().contains(name);
        }catch (Exception e){
            return false;
        }
    }

    public Sequence getSequence(String name){
        return new Sequence(this.metaSequenceTable,name);
    }


    public void sync(){
        this.environment.sync();
    }
	
	public synchronized void removeCollection(String name){
		containers.remove(name);
		Database db = openedDatabases.remove(name);
		if(db != null){
			db.close();
		}
		environment.removeDatabase(null, name);
	}
	
	public synchronized Database takeDatabase(String name){
		Database db = openedDatabases.get(name);
		if(db == null){
			DatabaseConfig conf =  makeDatabaseConfig();
			db = environment.openDatabase(null, name, conf);
			openedDatabases.put(name, db);
		}
		return db;
	}
	
	public synchronized Database takeDatabase(DatabaseConfig conf,String name){
		Database db = openedDatabases.get(name);
		if(db == null){
			db = environment.openDatabase(null, name, conf);
			openedDatabases.put(name, db);
		}
		return db;
	}
	
	
	public synchronized StoredMap<String, byte[]> getMap(String name){
		StoredMap<String, byte[]> map = (StoredMap<String, byte[]>)containers.get(name);
		if(map != null){
			return map;
		}
		ByteArrayBinding binding = new ByteArrayBinding();
		Database db = takeDatabase( name);
		map = new StoredMap<String, byte[]>(db, new StringBinding(), binding, true);
		containers.put(name, map);
		return map;
	}


	public synchronized BDBFireQueue getQueue(String name){
		DatabaseConfig conf = makeDatabaseConfig();
		Database db = takeDatabase(conf, name);
		BDBFireQueue queue = new BDBFireQueue (this,name);
		return queue;
	}
	
	public synchronized BDBFireList getList(String name){
		DatabaseConfig conf = makeDatabaseConfig();
		Database db = takeDatabase(conf, name);
		BDBFireList  list = new BDBFireList (db,name,new Sequence(this.metaSequenceTable,name));
		return list;
	}
	
	public synchronized StoredKeySet<byte[]> getSet(String name){
		StoredKeySet<byte[]> set = (StoredKeySet<byte[]>)containers.get(name);
		if(set != null){
			return set;
		}
		ByteArrayBinding binding = new ByteArrayBinding();
		Database db = takeDatabase( name);
		set = new StoredKeySet<byte[]>(db, binding, true);
		containers.put(name, set);
		return set;
	}
	
	public synchronized StoredSortedKeySet<byte[]> getSortedSet(String name){
		StoredSortedKeySet<byte[]> set = (StoredSortedKeySet<byte[]>)containers.get(name);
		if(set != null){
			return set;
		}
		ByteArrayBinding binding = new ByteArrayBinding();
        DatabaseConfig conf =  makeDatabaseConfig();
		Database db = takeDatabase(conf,name);
		set = new StoredSortedKeySet<byte[]>(db, binding, true);
		containers.put(name, set);
		return set;
	}

    public synchronized <K> StoredSortedKeySet<K> getSortedSet(String name,com.sleepycat.bind.EntryBinding<K> keyBinding){
        StoredSortedKeySet<K> set = (StoredSortedKeySet<K>)containers.get(name);
        if(set != null){
            return set;
        }
        DatabaseConfig conf =  makeDatabaseConfig();
        Database db = takeDatabase(conf, name);
        set = new StoredSortedKeySet<K>(db, keyBinding, true);
        containers.put(name, set);
        return set;
    }
	
	public synchronized StoredSortedMap<byte[], byte[]> getSortedMap(String name){
		StoredSortedMap<byte[], byte[]> map = (StoredSortedMap<byte[], byte[]>)containers.get(name);
		if(map != null){
			return map;
		}
		ByteArrayBinding binding = new ByteArrayBinding();
		Database db = takeDatabase( name);
		map = new StoredSortedMap<byte[], byte[]>(db, binding, binding, true);
		containers.put(name, map);
		return map;
	}

    public synchronized <K,V> StoredSortedMap<K, V> getSortedMap(String name,com.sleepycat.bind.EntryBinding<K> keyBinding, com.sleepycat.bind.EntryBinding<V> valueBinding){
        StoredSortedMap<K,V> map = (StoredSortedMap<K, V>)containers.get(name);
        if(map != null){
            return map;
        }
        Database db = takeDatabase( name);
        map = new StoredSortedMap<K, V>(db, keyBinding, valueBinding, true);
        containers.put(name, map);
        return map;
    }

    public synchronized <K,V> StoredMap<K, V> getMap(String name,com.sleepycat.bind.EntryBinding<K> keyBinding, com.sleepycat.bind.EntryBinding<V> valueBinding){
        StoredMap<K,V> map = (StoredMap<K, V>)containers.get(name);
        if(map != null){
            return map;
        }
        Database db = takeDatabase( name);
        map = new StoredMap<K, V>(db, keyBinding, valueBinding, true);
        containers.put(name, map);
        return map;
    }

}
