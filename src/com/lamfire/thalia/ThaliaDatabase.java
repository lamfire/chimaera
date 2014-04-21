package com.lamfire.thalia;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

import com.lamfire.thalia.serializer.Serialization;
import com.lamfire.thalia.serializer.Serializer;

public class ThaliaDatabase {
	private String file; // 文件路径
	private Thalia db;
	private int cacheSize = -1;

	public ThaliaDatabase(String file, boolean enableLocking, boolean enableTransactions, boolean deleteFilesAfterClose,
			boolean enableCache, int cacheSize) throws IOException {
		this.file = file;
		this.cacheSize = cacheSize;

		ThaliaBuilder builder = ThaliaBuilder.openFile(file).closeOnExit();
		if (enableCache) {
			builder.enableMRUCache();
			builder.setMRUCacheSize(this.cacheSize);
		}
		if (!enableLocking) {
			builder.disableLocking();
		}
		if (!enableTransactions) {
			builder.disableTransactions();
		}
		if (deleteFilesAfterClose) {
			builder.deleteFilesAfterClose();
		}

		this.db = builder.build();
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
