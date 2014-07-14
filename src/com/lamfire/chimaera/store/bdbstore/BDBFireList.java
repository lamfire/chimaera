package com.lamfire.chimaera.store.bdbstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.hydra.exception.NotSupportedMethodException;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;

public class BDBFireList  implements Serializable,FireList {
	private static final long serialVersionUID = 3427799316155220967L;
	private transient Database db; // 数据库,用于保存值,使得支持队列持久化,无需序列化
	private transient StoredMap<Long, byte[]> dataMap; // 持久化Map,Key为指针位置,Value为值,无需序列化

    private final Lock lock = new ReentrantLock();

    private StoredClassCatalog classCatalog;
    private Database classCatalogDB;
    private String name;
    private Sequence sequence;

    public BDBFireList(BDBEngine engine,String name) {
        this(engine.takeDatabase(name),name,engine.getSequence(name));
    }
	
	public BDBFireList(Database db,String name,Sequence sequence) {
		this.db = db;
        this.name = name;
        this.sequence = sequence;
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
		dataMap = new StoredSortedMap<Long, byte[]>(db, TupleBinding.getPrimitiveBinding(Long.class), valueBinding, true);
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
        try{
            lock.lock();
            dataMap.clear();
            sequence.set(0);
        }finally {
            lock.unlock();
        }
    }

    @Override
	public byte[] get(int index) {
        try{
            lock.lock();
            byte[] bytes = dataMap.get((long)index);
            return bytes;
        }finally {
            lock.unlock();
        }
	}

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> list = new ArrayList<byte[]>(size);
        try{
            lock.lock();
            for (int i = fromIndex; i < list.size(); i++) {
                list.add(get(i));
                if (list.size() >= size) {
                    break;
                }
            }
        }finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public byte[] remove(int index) {
        throw new NotSupportedMethodException("cannot remove elements");
    }


    public boolean add(byte[] e) {
		try {
            lock.lock();
			dataMap.put(sequence.inrc(1), e); // 从尾部插入
        }finally {
            lock.unlock();
        }
        return true;
	}

    @Override
    public void set(int index, byte[] value) {
        try {
            lock.lock();
            dataMap.put((long) index, value);
        }finally {
            lock.unlock();
        }
    }


}
