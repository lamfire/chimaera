package com.lamfire.chimaera.store.filestore;


import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.apache.jdbm.Serialization;
import org.apache.jdbm.Serializer;

import java.io.IOException;
import java.util.*;

public class DiskDatabase {
	private String file; // 文件路径
	private DB db;
	private int cacheSize = -1;

	public DiskDatabase(String file, boolean enableLocking, boolean enableTransactions, boolean deleteFilesAfterClose,
                        boolean enableCache, int cacheSize) throws IOException {
		this.file = file;
		this.cacheSize = cacheSize;

		DBMaker maker = DBMaker.openFile(file).closeOnExit();
		if (enableCache) {
            maker.enableMRUCache();
            maker.setMRUCacheSize(this.cacheSize);
		}
		if (!enableLocking) {
            maker.disableLocking();
		}
		if (!enableTransactions) {
            maker.disableTransactions();
		}
		if (deleteFilesAfterClose) {
            maker.deleteFilesAfterClose();
		}

		this.db = maker.make();
	}

    public synchronized<K extends Comparable<K>, V> Map<K, V> getTreeMap(String name) {
        Map<K, V> map = this.db.getTreeMap(name);
        if (map == null) {
            map = this.db.createTreeMap(name);
        }
        return map;
    }

    public synchronized <K, V> Map<K, V> getTreeMap(String name,Comparator<K> comparator ,Serializer<K> keySerializer, Serializer<V> valSerializer) {
        Map<K, V> map = this.db.getTreeMap(name);
        if (map == null) {
            map = this.db.createTreeMap(name,comparator, keySerializer, valSerializer);
        }
        return map;
    }

	public synchronized <K, V> Map<K, V> getHashMap(String name) {
		Map<K, V> map = this.db.getHashMap(name);
		if (map == null) {
			map = this.db.createHashMap(name);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public synchronized <K, V> Map<K, V> getHashMap(String name, Serializer<V> serializer) {
		Map<K, V> map = this.db.getHashMap(name);
		if (map == null) {
			map = this.db.createHashMap(name, new Serialization(), serializer);
		}
		return map;
	}

	public synchronized <K, V> Map<K, V> getHashMap(String name, Serializer<K> keySerializer, Serializer<V> valSerializer) {
		Map<K, V> map = this.db.getHashMap(name);
		if (map == null) {
			map = this.db.createHashMap(name, keySerializer, valSerializer);
		}
		return map;
	}

	public synchronized <E> List<E> getLinkedList(String name, Serializer<E> serializer) {
		List<E> list = this.db.getLinkedList(name);
		if (list == null) {
			list = this.db.createLinkedList(name, serializer);
		}
		return list;
	}

    public synchronized <E> List<E> getLinkedList(String name) {
        List<E> list = this.db.getLinkedList(name);
        if (list == null) {
            list = this.db.createLinkedList(name);
        }
        return list;
    }

	public synchronized <E> Set<E> getHashSet(String name, Serializer<E> serializer) {
		Set<E> set = this.db.getHashSet(name);
		if (set == null) {
			set = this.db.createHashSet(name, serializer);
		}
		return set;
	}

	public synchronized <E> Set<E> getHashSet(String name) {
		Set<E> set = this.db.getHashSet(name);
		if (set == null) {
			set = this.db.createHashSet(name);
		}
		return set;
	}

	public synchronized <E> NavigableSet<E> getTreeSet(String name) {
		NavigableSet<E> set = this.db.getTreeSet(name);
		if (set == null) {
			set = this.db.createTreeSet(name);
		}
		return set;
	}

	public synchronized <E> NavigableSet<E> getTreeSet(String name, Comparator<E> comparator, Serializer<E> serializer) {
		NavigableSet<E> set = this.db.getTreeSet(name);
		if (set == null) {
			set = this.db.createTreeSet(name, comparator, serializer);
		}
		return set;
	}

	public synchronized void remove(String name) {
		this.db.deleteCollection(name);
		this.db.commit();
	}

	public synchronized void clear() {
		this.db.closeAndDeleteAllFiles();
		this.db.reopen();
	}

	public int size() {
		Map<String, Object> collections = this.db.getCollections();
		if (collections == null) {
			return 0;
		}
		return collections.size();
	}

	public Set<String> keys() {
		Map<String, Object> collections = this.db.getCollections();
		if (collections == null) {
			return new HashSet<String>();
		}
		return collections.keySet();
	}

	public boolean exists(String key) {
		Map<String, Object> collections = this.db.getCollections();
		if (collections == null) {
			return false;
		}
		return collections.containsKey(key);
	}

	public synchronized void flush() {
		try {
			db.commit();
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public synchronized void close() {
		db.commit();
		db.close();
	}

	public String getFile() {
		return file;
	}

    public synchronized void defrag() {
        this.db.defrag(true);
    }
}
