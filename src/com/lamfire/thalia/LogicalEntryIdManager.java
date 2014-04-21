package com.lamfire.thalia;

import java.io.IOException;
import java.util.Arrays;

import com.lamfire.thalia.storage.Storage;

/**
 * This class manages the linked lists of logical rowid pages.
 */
final class LogicalEntryIdManager {
    // our record file and associated page manager
    private final PageFile file;
    private final PageManager pageman;
    public static final short ELEMS_PER_PAGE = (short) ((Storage.PAGE_SIZE - Const.PAGE_HEADER_SIZE) / Const.PhysicalRowId_SIZE);

    private long[] freeRecordsInTransRowid = new long[4];
    private int freeRecordsInTransSize = 0;


    /** number of free logical rowids on logical free page, is SHORT*/
    static final int OFFSET_FREE_COUNT = Const.PAGE_HEADER_SIZE;
    static final int FREE_HEADER_SIZE = Const.PAGE_HEADER_SIZE + Const.SZ_SHORT;
    /** maximal number of free logical per page */
    static final int FREE_RECORDS_PER_PAGE = (Storage.PAGE_SIZE -FREE_HEADER_SIZE)/6;


    /**
     * Creates a log rowid manager using the indicated record file and page manager
     */
    LogicalEntryIdManager(PageFile file, PageManager pageman) throws IOException {
        this.file = file;
        this.pageman = pageman;
    }

    /**
     * Creates a new logical rowid pointing to the indicated physical id
     *
     * @param physloc physical location to point to
     * @return logical recid
     */
    long insert(final long physloc) throws IOException {
        // check whether there's a free rowid to reuse
        long retval = getFreeSlot();
        if (retval == 0) {
            // no. This means that we bootstrap things by allocating
            // a new translation page and freeing all the rowids on it.
            long firstPage = pageman.allocate(Const.TRANSLATION_PAGE);
            short curOffset = Const.PAGE_HEADER_SIZE;
            for (int i = 0; i < ELEMS_PER_PAGE; i++) {
                putFreeSlot(((-firstPage) << Storage.PAGE_SIZE_SHIFT) + (long) curOffset);

                curOffset += Const.PhysicalRowId_SIZE;
            }

            retval = getFreeSlot();
            if (retval == 0) {
                throw new Error("couldn't obtain free translation");
            }
        }
        // write the translation.
        update(retval, physloc);
        return retval;
    }

    /**
     * Insert at forced location, use only for defragmentation !!
     *
     * @param logicalRowId
     * @param physLoc
     * @throws java.io.IOException
     */
    void forceInsert(final long logicalRowId, final long physLoc) throws IOException {
        if (fetch(logicalRowId) != 0)
            throw new Error("can not forceInsert, record already exists: " + logicalRowId);

        update(logicalRowId, physLoc);
    }


    /**
     * Releases the indicated logical rowid.
     */
    void delete(final long logicalrowid) throws IOException {
        //zero out old location, is needed for defragmentation
        final long pageId = -(logicalrowid>>> Storage.PAGE_SIZE_SHIFT);
        final PageIo xlatPage = file.get(pageId);
        xlatPage.pageHeaderSetLocation((short) (logicalrowid & Storage.OFFSET_MASK), 0);
        file.release(pageId, true);
        putFreeSlot(logicalrowid);
    }

    /**
     * Updates the mapping
     *
     * @param logicalrowid The logical rowid
     * @param physloc   The physical rowid
     */
    void update(final long logicalrowid, final long physloc) throws IOException {

        final long pageId =  -(logicalrowid>>> Storage.PAGE_SIZE_SHIFT);
        final PageIo xlatPage = file.get(pageId);
        xlatPage.pageHeaderSetLocation((short) (logicalrowid & Storage.OFFSET_MASK), physloc);
        file.release(pageId, true);
    }

    /**
     * Returns a mapping
     *
     * @param logicalrowid The logical rowid
     * @return The physical rowid, 0 if does not exist
     */
    long fetch(long logicalrowid) throws IOException {
        final long pageId = -(logicalrowid>>> Storage.PAGE_SIZE_SHIFT);
        final long last = pageman.getLast(Const.TRANSLATION_PAGE);
        if (last - 1 > pageId)
            return 0;

        final short offset = (short) (logicalrowid & Storage.OFFSET_MASK);

        final PageIo xlatPage = file.get(pageId);
        final long ret =  xlatPage.pageHeaderGetLocation(offset);


        file.release(pageId, false);
        return ret;
    }

    void commit() throws IOException {
        if(freeRecordsInTransSize==0) return;

        long freeRecPageId = pageman.getLast(Const.FREELOGIDS_PAGE);
        if(freeRecPageId == 0){
            //allocate new
            freeRecPageId = pageman.allocate(Const.FREELOGIDS_PAGE);
        }
        PageIo freeRecPage = file.get(freeRecPageId);
        //write all uncommited free records
        for(int rowPos = 0;rowPos<freeRecordsInTransSize;rowPos++){
            short count = freeRecPage.readShort(OFFSET_FREE_COUNT);
            if(count == FREE_RECORDS_PER_PAGE){
                //allocate new free recid page
                file.release(freeRecPage);
                freeRecPageId = pageman.allocate(Const.FREELOGIDS_PAGE);
                freeRecPage = file.get(freeRecPageId);
                freeRecPage.writeShort(FREE_RECORDS_PER_PAGE, (short)0);
                count = 0;
            }
            final int offset =  (count ) *6 + FREE_HEADER_SIZE;
            //write free recid and increase counter
            freeRecPage.writeSixByteLong(offset,freeRecordsInTransRowid[rowPos]);
            count++;
            freeRecPage.writeShort(OFFSET_FREE_COUNT, count);

        }
        file.release(freeRecPage);

        clearFreeRecidsInTransaction();
    }

    private void clearFreeRecidsInTransaction() {
        if(freeRecordsInTransRowid.length>128)
            freeRecordsInTransRowid = new long[4];
        freeRecordsInTransSize = 0;
    }

    void rollback() throws IOException {
        clearFreeRecidsInTransaction();
    }


    /**
     * Returns a free Logical rowid, or
     * 0 if nothing was found.
     */
    long getFreeSlot() throws IOException {
        if (freeRecordsInTransSize != 0) {
            return freeRecordsInTransRowid[--freeRecordsInTransSize];
        }

        final long logicFreePageId = pageman.getLast(Const.FREELOGIDS_PAGE);
        if(logicFreePageId == 0) {
            return 0;
        }
        PageIo logicFreePage = file.get(logicFreePageId);
        short recCount = logicFreePage.readShort(OFFSET_FREE_COUNT);
        if(recCount <= 0){
            throw new InternalError();
        }


        final int offset = (recCount -1) *6 + FREE_HEADER_SIZE;
        final long ret = logicFreePage.readSixByteLong(offset);

        recCount--;

        if(recCount>0){
            //decrease counter and zero out old record
            logicFreePage.writeSixByteLong(offset,0);
            logicFreePage.writeShort(OFFSET_FREE_COUNT, recCount);
            file.release(logicFreePage);
        }else{
            //release this page
            file.release(logicFreePage);
            pageman.free(Const.FREELOGIDS_PAGE,logicFreePageId);
        }

        return ret;
    }

    /**
     * Puts the indicated rowid on the free list
     */
    void putFreeSlot(long rowid) throws IOException {
        //ensure capacity
        if(freeRecordsInTransSize == freeRecordsInTransRowid.length)
            freeRecordsInTransRowid = Arrays.copyOf(freeRecordsInTransRowid, freeRecordsInTransRowid.length * 4);
        //add record and increase size
        freeRecordsInTransRowid[freeRecordsInTransSize]=rowid;
        freeRecordsInTransSize++;
    }



}
