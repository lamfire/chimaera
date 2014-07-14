package com.lamfire.chimaera.store.bdbstore;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import com.lamfire.chimaera.store.FireQueue;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;

public class BDBFireQueue extends AbstractQueue<byte[]> implements Serializable,FireQueue {

	private static final long serialVersionUID = 3427799316155220967L;
	private transient Database db; // 数据库,用于保存值,使得支持队列持久化,无需序列化
	private transient StoredMap<Long, byte[]> queueMap; // 持久化Map,Key为指针位置,Value为值,无需序列化
	private AtomicLong headIndex; // 头部指针
	private AtomicLong tailIndex; // 尾部指针
	private transient byte[] peekItem = null; // 当前获取的值

    private StoredClassCatalog classCatalog;
    private Database classCatalogDB;
    private String name;

	/**
	 * 构造函数,传入BDB数据库
	 * 
	 * @param db
	 */
	public BDBFireQueue(Database db,String name) {
		this.db = db;
        this.name = name;
		headIndex = new AtomicLong(0);
		tailIndex = new AtomicLong(0);
		bindDatabase(db, byte[].class, getClassCatalog());
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

	public void bindDatabase(Database db, Class<byte[]> valueClass, StoredClassCatalog classCatalog) {
		EntryBinding<byte[]> valueBinding = TupleBinding.getPrimitiveBinding(valueClass);
		if (valueBinding == null) {
			valueBinding = new SerialBinding<byte[]>(classCatalog, valueClass); // 序列化绑定
		}
		queueMap = new StoredSortedMap<Long, byte[]>(db,TupleBinding.getPrimitiveBinding(Long.class),valueBinding,true);
	}

	/**
	 * 值遍历器
	 */
	@Override
	public Iterator<byte[]> iterator() {
		return queueMap.values().iterator();
	}

	/**
	 * 大小
	 */
	@Override
	public int size() {
		synchronized (tailIndex) {
			synchronized (headIndex) {
				return (int) (tailIndex.get() - headIndex.get());
			}
		}
	}

	/**
	 * 插入值
	 */
	@Override
	public boolean offer(byte[] e) {
		synchronized (tailIndex) {
			queueMap.put(tailIndex.getAndIncrement(), e); // 从尾部插入
		}
		return true;
	}

    @Override
    public void push(byte[] value) {
        offer(value);
    }

    @Override
    public byte[] pop() {
        return poll();
    }

    /**
	 * 获取值,从头部获取
	 */
	@Override
	public byte[] peek() {
		synchronized (headIndex) {
			if (peekItem != null) {
				return peekItem;
			}
			byte[] headItem = null;
			while (headItem == null && headIndex.get() < tailIndex.get()) { // 没有超出范围
				headItem = queueMap.get(headIndex.get());
				if (headItem != null) {
					peekItem = headItem;
					continue;
				}
				headIndex.incrementAndGet(); // 头部指针后移
			}
			return headItem;
		}
	}

	/**
	 * 移出元素,移出头部元素
	 */
	@Override
	public byte[] poll() {
		synchronized (headIndex) {
            byte[] headItem = peek();
			if (headItem != null) {
				queueMap.remove(headIndex.getAndIncrement());
				peekItem = null;
				return headItem;
			}
		}
		return null;
	}

	/**
	 * 关闭,也就是关闭所是用的BDB数据库但不关闭数据库环境
	 */
	public void close() {
		try {
			if (db != null) {
				db.sync();
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
	}

	/**
	 * 清理,会清空数据库,慎用.如果想保留数据,请调用close()
	 */
	@Override
	public void clear() {
        synchronized (tailIndex) {
            synchronized (headIndex) {
                headIndex.set(0);
                tailIndex.set(0);
                queueMap.clear();
            }
        }
	}
}
