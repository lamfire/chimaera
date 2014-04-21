package com.lamfire.thalia;

import com.lamfire.thalia.storage.Storage;

/**
 * This interface contains magic cookies.
 */
public interface Const {
	/**
	 * Const cookie at start of file
	 */
	final short FILE_HEADER = 0x1350;

	/**
	 * Const for pages. They're offset by the page type magic codes.
	 */
	final short PAGE_MAGIC = 0x1351;

	/**
	 * Const for pages in certain lists.
	 */
	final short FREE_PAGE = 0;
	final short USED_PAGE = 1;
	final short TRANSLATION_PAGE = 2;
	final short FREELOGIDS_PAGE = 3;
	final short FREEPHYSIDS_PAGE = 4;
	final short FREEPHYSIDS_ROOT_PAGE = 5;

	/**
	 * Number of lists in a file
	 */
	final short NLISTS = 6;

	/**
	 * Const for transaction file
	 */
	final short LOGFILE_HEADER = 0x1360;

	/**
	 * Size of an externalized byte
	 */
	final short SZ_BYTE = 1;
	/**
	 * Size of an externalized short
	 */
	final short SZ_SHORT = 2;

	/**
	 * Size of an externalized int
	 */
	final short SZ_INT = 4;
	/**
	 * Size of an externalized long
	 */
	final short SZ_LONG = 8;

	/**
	 * size of three byte integer
	 */
	final short SZ_SIX_BYTE_LONG = 6;

	/** offsets in file header (zero page in file) */
	final short FILE_HEADER_O_MAGIC = 0; // short magic
	final short FILE_HEADER_O_LISTS = Const.SZ_SHORT; // long[2*NLISTS]
	final int FILE_HEADER_O_ROOTS = FILE_HEADER_O_LISTS + (Const.NLISTS * 2 * Const.SZ_LONG);
	/**
	 * The number of "root" rowids available in the file.
	 */
	final int FILE_HEADER_NROOTS = 16;

	final short PAGE_HEADER_O_MAGIC = 0; // short magic
	final short PAGE_HEADER_O_NEXT = Const.SZ_SHORT;
	final short PAGE_HEADER_O_PREV = PAGE_HEADER_O_NEXT + Const.SZ_SIX_BYTE_LONG;
	final short PAGE_HEADER_SIZE = PAGE_HEADER_O_PREV + Const.SZ_SIX_BYTE_LONG;

	final short PhysicalRowId_O_LOCATION = 0; // long page
	// short PhysicalRowId_O_OFFSET = Const.SZ_SIX_BYTE_LONG; // short
	// offset
	final int PhysicalRowId_SIZE = Const.SZ_SIX_BYTE_LONG;

	final short DATA_PAGE_O_FIRST = PAGE_HEADER_SIZE; // short firstrowid
	final short DATA_PAGE_O_DATA = (short) (DATA_PAGE_O_FIRST + Const.SZ_SHORT);
	final short DATA_PER_PAGE = (short) (Storage.PAGE_SIZE - DATA_PAGE_O_DATA);

}
