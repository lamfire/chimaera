package com.lamfire.chimaera.store.bdbstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.hydra.exception.NotSupportedMethodException;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;

public class BDBFireList  implements Serializable,FireList {

	private static final long serialVersionUID = 3427799316155220967L;
	private transient Database db; // 数据库,用于保存值,使得支持队列持久化,无需序列化
	private transient StoredMap<Long, byte[]> dataMap; // 持久化Map,Key为指针位置,Value为值,无需序列化

    private StoredClassCatalog classCatalog;
    private Database classCatalogDB;
	private AtomicLong index = new AtomicLong(); // 尾部指针
    private String name;

	
	public BDBFireList(Database db,String name) {
		this.db = db;
        this.name = name;
		this.classCatalog = getClassCatalog();
		bindDatabase(db, byte[].class, classCatalog);
	}

    private  StoredClassCatalog getClassCatalog() {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		try {
			this.classCatalogDB = db.getEnvironment().openDatabase(null, db.getDatabaseName() + "_classCatalog", dbConfig);
			StoredClassCatalog classCatalog = new StoredClassCatalog(classCatalogDB);
			return classCatalog;
		} catch (DatabaseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 绑定数据库
	 * 
	 * @param db
	 * @param valueClass
	 * @param classCatalog
	 */
	private void bindDatabase(Database db, Class<byte[]> valueClass, StoredClassCatalog classCatalog) {
		EntryBinding<byte[]> valueBinding = TupleBinding.getPrimitiveBinding(valueClass);
		if (valueBinding == null) {
			valueBinding = new SerialBinding<byte[]>(classCatalog, valueClass); // 序列化绑定
		}
		this.db = db;
		dataMap = new StoredSortedMap<Long, byte[]>(db, // db
				TupleBinding.getPrimitiveBinding(Long.class), // Key
				valueBinding, // Value
				true); // allow write
	}

	/**
	 * 值遍历器
	 */
	public Iterator<byte[]> iterator() {
		return dataMap.values().iterator();
	}

	/**
	 * 大小
	 */
	@Override
	public int size() {
		return (int)db.count();
	}

    @Override
    public void clear() {
        dataMap.clear();
    }

    @Override
	public byte[] get(int index) {
		return dataMap.get((long)index);
	}

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> list = new ArrayList<byte[]>(size);
        for (int i = fromIndex; i < list.size(); i++) {
            list.add(get(i));
            if (list.size() >= size) {
                break;
            }
        }
        return list;
    }

    @Override
    public byte[] remove(int index) {
        throw new NotSupportedMethodException("cannot remove elements");
    }


    public boolean add(byte[] e) {
		synchronized (index) {
			dataMap.put(index.getAndIncrement(), e); // 从尾部插入
		}
        return true;
	}

    @Override
    public void set(int index, byte[] value) {
        dataMap.put((long) index, value);
    }


}
