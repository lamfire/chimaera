package com.lamfire.thalia.storage;

import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 */
public interface Storage {
	public static final String EXTENSION_IDNEX_FILE = ".i";
	public static final String EXTENSION_DATA_FILE = ".d";
	public static final String EXTENSION_TRANSACTION_FILE = ".t";

	/**
	 * Bite shift used to calculate page size. If you want to modify page
	 * size, do it here.
	 * 
	 * 1<<9 = 512 1<<10 = 1024 1<<11 = 2048 1<<12 = 4096
	 */
	int PAGE_SIZE_SHIFT = 12;

	/**
	 * the lenght of single page.
	 * <p>
	 * !!! DO NOT MODIFY THI DIRECTLY !!!
	 */
	int PAGE_SIZE = 1 << PAGE_SIZE_SHIFT;

	/**
	 * use 'val & OFFSET_MASK' to quickly get offset within the page;
	 */
	long OFFSET_MASK = 0xFFFFFFFFFFFFFFFFL >>> (64 - Storage.PAGE_SIZE_SHIFT);

	void write(long pageNumber, ByteBuffer data) throws IOException;

	ByteBuffer read(long pageNumber) throws IOException;

	void forceClose() throws IOException;

	boolean isReadonly();

	DataInputStream readTransactionLog();

	void deleteTransactionLog();

	void sync() throws IOException;

	DataOutputStream openTransactionLog() throws IOException;

	void deleteAllFiles() throws IOException;
}
