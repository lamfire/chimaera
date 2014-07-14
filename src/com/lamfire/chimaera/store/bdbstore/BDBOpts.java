package com.lamfire.chimaera.store.bdbstore;

public class BDBOpts {

	public static final long MS_PER_SECOND = 1000;
	public static final long US_PER_MS = 1000;

	private long cacheSize = 16 * 1024 * 1024; //16MB
	private long logFileSize = 1024 * 1024 * 1024; //1G
	private boolean isTransactionEnabled = false;
	private boolean isFlushTransaction = false;
	private int btreeFanout = 512;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * BDB cache size in bytes.
	 * 
	 * @return BDB cache size in bytes.
	 */
	public long getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(long size) {
		this.cacheSize = size;
	}

	/**
	 * Returns true if the write transaction is enabled; false, otherwise.
	 * 
	 * @return true if the write transaction is enabled; false, otherwise.
	 */
	public boolean isWriteTransactionsEnabled() {
		return isTransactionEnabled;
	}

	public void setWriteTransactionEnabled(boolean enabled) {
		this.isTransactionEnabled = enabled;
	}

	/**
	 * A boolean indicates if the data should be flushed to disk when a write
	 * transaction completes.
	 * 
	 * @return a boolean indicates if the data should be flushed to disk when a
	 *         transaction completes.
	 */
	public boolean isFlushTransactionsEnabled() {
		return isFlushTransaction;
	}

	public void setFlushTransactionEnabled(boolean enabled) {
		this.isFlushTransaction = enabled;
	}

	/**
	 * BDB file size in bytes.
	 * 
	 * @return the number of bytes each BDB file has.
	 */
	public long getMaxLogFileSize() {
		return logFileSize;
	}

	public void setMaxLogFileSize(long size) {
		this.logFileSize = size;
	}

	/**
	 * BDB btree breadth.
	 * 
	 * @return the btree breadth
	 */
	public int getBtreeFanout() {
		return btreeFanout;
	}

	public void setBtreeFanout(int fanout) {
		this.btreeFanout = fanout;
	}

	/**
	 * BDB checkpoint
	 */
	public long getCheckpointBytes() {
		return 20 * 1024 * 1024;
	}

	public long getCheckpointMs() {
		return 30 * MS_PER_SECOND;
	}
}
