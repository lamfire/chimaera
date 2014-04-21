package com.lamfire.thalia;

import java.io.IOException;
import java.util.Arrays;

import com.lamfire.thalia.storage.Storage;

/**
 * This class manages free physical rowid pages and provides methods to free and allocate physical rowids on a high
 * level.
 */
final class PhysicalFreeEntryIdManager {

    /** maximal record size which can be hold. If record crosses multiple pages, it is trimmed before added to free list */
    static final int MAX_REC_SIZE = Storage.PAGE_SIZE *2;

    /** where data on root page starts, there are no extra data in page header */
    static final int ROOT_HEADER_SIZE = Const.PAGE_HEADER_SIZE;

    /** page header size for slot page */
    static final int SLOT_PAGE_HEADER_SIZE = Const.PAGE_HEADER_SIZE + Const.SZ_SHORT + Const.SZ_SIX_BYTE_LONG;

    /**  number of recids on slot page */
    static final int OFFSET_SLOT_PAGE_REC_COUNT = Const.PAGE_HEADER_SIZE;

    static final int SLOT_PAGE_REC_NUM = (Storage.PAGE_SIZE - SLOT_PAGE_HEADER_SIZE)/6;

    /** pointer to next slo page in slot page header */
    static final int OFFSET_SLOT_PAGE_NEXT = Const.PAGE_HEADER_SIZE + Const.SZ_SHORT;

    /** number of size slots held in root page */
    static final int MAX_RECIDS_PER_PAGE = (Storage.PAGE_SIZE -ROOT_HEADER_SIZE-6) / 6; //6 is size of page pointer

    /** free records are grouped into slots by record size. Here is max diff in record size per group */
    static final int ROOT_SLOT_SIZE = 1+MAX_REC_SIZE/ MAX_RECIDS_PER_PAGE;


    protected final PageFile file;

    protected final PageManager pageman;

    /** list of free phys slots in current transaction. First two bytes are size, last 6 bytes are recid*/
    private long[] inTrans = new long[8];
    private int inTransSize = 0;

    /**
     * Creates a new instance using the indicated record file and page manager.
     */
    PhysicalFreeEntryIdManager(PageFile file, PageManager pageman) throws IOException {
        this.file = file;
        this.pageman = pageman;
    }

    long getFreeRecord(final int size) throws IOException {
        if(size >= MAX_REC_SIZE) return 0;

        final PageIo root = getRootPage();
        final int  rootPageOffset = sizeToRootOffset(size+ ROOT_SLOT_SIZE);
        final long slotPageId = root.readSixByteLong(rootPageOffset);

        if(slotPageId==0){
            file.release(root);
            return 0;
        }

        PageIo slotPage = file.get(slotPageId);
        if(slotPage.readShort(Const.PAGE_HEADER_O_MAGIC) != Const.PAGE_MAGIC + Const.FREEPHYSIDS_PAGE)
            throw new InternalError();

        short recidCount = slotPage.readShort(OFFSET_SLOT_PAGE_REC_COUNT);
        if(recidCount<=0){
            throw new InternalError();
        }

        final int offset = (recidCount-1) * 6 + SLOT_PAGE_HEADER_SIZE;
        final long recid = slotPage.readSixByteLong(offset);

        recidCount --;
        if(recidCount>0){
            //decrease counter and zero out old record
            slotPage.writeSixByteLong(offset,0);
            slotPage.writeShort(OFFSET_SLOT_PAGE_REC_COUNT, recidCount);
            file.release(root);
            file.release(slotPage);
        }else{
            //release this page
            long prevSlotPageId = slotPage.readSixByteLong(OFFSET_SLOT_PAGE_NEXT);
            root.writeSixByteLong(rootPageOffset,prevSlotPageId);
            file.release(root);
            file.release(slotPage);
            pageman.free(Const.FREEPHYSIDS_PAGE,slotPageId);

        }

        return recid;
    }

    static final  int sizeToRootOffset(int size) {
        return ROOT_HEADER_SIZE + 6 * (size/ROOT_SLOT_SIZE);
    }


    /**
     * Puts the indicated rowid on the free list, which awaits for commit
     */
    void putFreeRecord(final long rowid, final int size) throws IOException {
        //ensure capacity
        if(inTransSize==inTrans.length){
            inTrans = Arrays.copyOf(inTrans, inTrans.length * 2);
        }
        inTrans[inTransSize] = rowid + (((long)size)<<48);
        inTransSize++;
    }


    public void commit() throws IOException {

        if(inTransSize==0)
            return;

        Arrays.sort(inTrans,0,inTransSize-1);


        //write all uncommited free records
        final PageIo root = getRootPage();
        PageIo slotPage = null;
        for(int rowIdPos = 0; rowIdPos<inTransSize; rowIdPos++){
                final int size = (int) (inTrans[rowIdPos] >>>48);

                final long rowid = inTrans[rowIdPos] & 0x0000FFFFFFFFFFFFL;
                final int rootPageOffset = sizeToRootOffset(size);

                long slotPageId  = root.readSixByteLong(rootPageOffset);
                if(slotPageId == 0){
                        if(slotPage!=null) file.release(slotPage);
                        //create new page for this slot
                        slotPageId = pageman.allocate(Const.FREEPHYSIDS_PAGE);
                        root.writeSixByteLong(rootPageOffset,slotPageId);
                }

                if(slotPage == null || slotPage.getPageId()!=slotPageId){
                    if(slotPage!=null) file.release(slotPage);
                    slotPage = file.get(slotPageId);
                }
                if(slotPage.readShort(Const.PAGE_HEADER_O_MAGIC) != Const.PAGE_MAGIC + Const.FREEPHYSIDS_PAGE)
                    throw new InternalError();

                short recidCount = slotPage.readShort(OFFSET_SLOT_PAGE_REC_COUNT);
                if(recidCount== MAX_RECIDS_PER_PAGE){
                    file.release(slotPage);
                    //allocate new slot page and update links
                    final long newSlotPageId = pageman.allocate(Const.FREEPHYSIDS_PAGE);
                    slotPage = file.get(newSlotPageId);
                    slotPage.writeSixByteLong(OFFSET_SLOT_PAGE_NEXT,slotPageId);
                    slotPage.writeShort(OFFSET_SLOT_PAGE_REC_COUNT,(short)0);
                    recidCount = 0;
                    slotPageId = newSlotPageId;
                    root.writeSixByteLong(rootPageOffset,newSlotPageId);
                }

                //write new recid
                slotPage.writeSixByteLong(recidCount * 6 + SLOT_PAGE_HEADER_SIZE,rowid);

                //and increase count
                recidCount++;
                slotPage.writeShort(OFFSET_SLOT_PAGE_REC_COUNT,recidCount);

        }
        if(slotPage!=null){
            file.release(slotPage);
        }
        file.release(root);
        clearFreeInTrans();
    }


    public void rollback() {
        clearFreeInTrans();
    }

    private void clearFreeInTrans() {
        if(inTrans.length>128)
            inTrans = new long[8];
        inTransSize = 0;
    }

    /** return free phys row page. If not found create it */
    final PageIo getRootPage() throws IOException {
        long pageId = pageman.getFirst(Const.FREEPHYSIDS_ROOT_PAGE);
        if(pageId == 0){
            pageId = pageman.allocate(Const.FREEPHYSIDS_ROOT_PAGE);
        }
        return file.get(pageId);
    }
}
