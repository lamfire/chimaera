package com.lamfire.thalia.tree;

import java.io.IOError;
import java.io.IOException;

import com.lamfire.thalia.ThaliaBase;
import com.lamfire.thalia.serializer.Serializer;

public class BTreeLazyRecord<E> {
	/**
	 * if value in tree is serialized in more bytes, it is stored as
	 * separate record outside of tree This value must be always smaller
	 * than 250
	 */
	public static final int MAX_INTREE_RECORD_SIZE = 32;
	public static final int NULL = 255;
	public static final int LAZY_RECORD = 254;
	
	
	private E value = null;
	private ThaliaBase db;
	private Serializer<E> serializer;
	final long recid;

	BTreeLazyRecord(ThaliaBase db, long recid, Serializer<E> serializer) {
		this.db = db;
		this.recid = recid;
		this.serializer = serializer;
	}

	E get() {
		if (value != null)
			return value;
		try {
			value = db.fetch(recid, serializer);
		} catch (IOException e) {
			throw new IOError(e);
		}
		return value;
	}

	void delete() {
		try {
			db.delete(recid);
		} catch (IOException e) {
			throw new IOError(e);
		}
		value = null;
		serializer = null;
		db = null;
	}

}
