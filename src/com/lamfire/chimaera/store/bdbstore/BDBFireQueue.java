package com.lamfire.chimaera.store.bdbstore;

import java.io.IOError;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private static final String HEAD_SEQUENCE_NAME_SUFFIX = "_HEAD_SEQUENCE";
    private static final String TAIL_SEQUENCE_NAME_SUFFIX = "_TAIL_SEQUENCE";
	private transient Database db; // 数据库,用于保存值,使得支持队列持久化,无需序列化
	private transient StoredMap<Long, byte[]> queueMap; // 持久化Map,Key为指针位置,Value为值,无需序列化
	private transient byte[] peekItem = null; // 当前获取的值

    private final Lock lock = new ReentrantLock();

    private StoredClassCatalog classCatalog;
    private Database classCatalogDB;
    private String name;

    private Sequence headSequence;
    private Sequence tailSequence;

    public BDBFireQueue(BDBEngine engine,String name) {
        this(engine.takeDatabase(name),name,engine.getSequence(name+HEAD_SEQUENCE_NAME_SUFFIX),engine.getSequence(name+TAIL_SEQUENCE_NAME_SUFFIX));
    }

	/**
	 * 构造函数,传入BDB数据库
	 * 
	 * @param db
	 */
	public BDBFireQueue(Database db,String name,Sequence headSequence,Sequence tailSequence) {
		this.db = db;
        this.name = name;
		this.headSequence  = headSequence;
		this.tailSequence = tailSequence;
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
		try{
            lock.lock();
            long last = tailSequence.get();
            long first = headSequence.get();
            if(last < 0 && first > 0){
                return (int) ((Long.MAX_VALUE - first) + (Long.MIN_VALUE - last));
            }
			return (int)Math.abs(last - first);
		}finally {
            lock.unlock();
        }
	}

	/**
	 * 插入值
	 */
	@Override
	public boolean offer(byte[] e) {
        try{
            lock.lock();
			queueMap.put(tailSequence.inrc(1), e); // 从尾部插入
        }finally {
            lock.unlock();
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
        try{
            lock.lock();
			if (peekItem != null) {
				return peekItem;
			}
			byte[] headItem = null;
			while (headItem == null && headSequence.get() < tailSequence.get()) { // 没有超出范围
				headItem = queueMap.get(headSequence.get());
				if (headItem != null) {
					peekItem = headItem;
					continue;
				}
                headSequence.inrc(1);
			}
			return headItem;
		}finally {
            lock.unlock();
        }
	}

	/**
	 * 移出元素,移出头部元素
	 */
	@Override
	public byte[] poll() {
        try{
            lock.lock();
            byte[] headItem = peek();
			if (headItem != null) {
				queueMap.remove(headSequence.inrc(1));
				peekItem = null;
				return headItem;
			}
        }finally {
            lock.unlock();
        }
		return null;
	}

	/**
	 * 关闭,也就是关闭所是用的BDB数据库但不关闭数据库环境
	 */
	public void close() {
		try {
            lock.lock();
			if (db != null) {
				db.sync();
				db.close();
			}
		} catch (Exception e) {
			throw new IOError(e);
		}finally {
            lock.unlock();
        }
	}

	/**
	 * 清理,会清空数据库,慎用.如果想保留数据,请调用close()
	 */
	@Override
	public void clear() {
        try {
            lock.lock();
            headSequence.set(0);
            tailSequence.set(0);
            queueMap.clear();
        }finally {
            lock.unlock();
        }
	}
}
