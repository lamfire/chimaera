package com.lamfire.thalia;

import java.io.IOException;

import com.lamfire.thalia.storage.Storage;
import com.lamfire.thalia.stream.DataInputOutput;

import static com.lamfire.thalia.storage.Storage.*;

/**
 * This class manages physical row ids, and their data.
 */
final class PhysicalEntryIdManager {

    // The file we're talking to and the associated page manager.
    final private PageFile file;
    final private PageManager pageman;
    final PhysicalFreeEntryIdManager freeman;
    static final private short DATA_PER_PAGE = (short) (PAGE_SIZE - Const.DATA_PAGE_O_DATA);
    //caches offset after last allocation. So we dont have to iterate throw page every allocation
    private long cachedLastAllocatedRecordPage = Long.MIN_VALUE;
    private short cachedLastAllocatedRecordOffset = Short.MIN_VALUE;

    /**
     * Creates a new rowid manager using the indicated record file. and page manager.
     */
    PhysicalEntryIdManager(PageFile file, PageManager pageManager) throws IOException {
        this.file = file;
        this.pageman = pageManager;
        this.freeman = new PhysicalFreeEntryIdManager(file, pageManager);

    }


    /**
     * Inserts a new record. Returns the new physical rowid.
     */
    long insert(final byte[] data, final int start, final int length) throws IOException {
        if (length < 1)
            throw new IllegalArgumentException("Length is <1");
        if (start < 0)
            throw new IllegalArgumentException("negative start");

        long retval = alloc(length);
        write(retval, data, start, length);
        return retval;
    }

    /**
     * Updates an existing record. Returns the possibly changed physical rowid.
     */
    long update(long rowid, final byte[] data, final int start, final int length) throws IOException {
        // fetch the record header
        PageIo page = file.get(rowid>>> Storage.PAGE_SIZE_SHIFT);
        short head =  (short) (rowid & Storage.OFFSET_MASK);
        int availSize = EntryHeader.getAvailableSize(page, head);
        if (length > availSize ||
                //difference between free and available space can be only 254.
                //if bigger, need to realocate and free page
                availSize - length > EntryHeader.MAX_SIZE_SPACE
                ) {
            // not enough space - we need to copy to a new rowid.
            file.release(page);
            free(rowid);
            rowid = alloc(length);
        } else {
            file.release(page);
        }

        // 'nuff space, write it in and return the rowid.
        write(rowid, data, start, length);
        return rowid;
    }


    void fetch(final DataInputOutput out, final long rowid) throws IOException {
        // fetch the record header
        long current = rowid >>> Storage.PAGE_SIZE_SHIFT;
        PageIo page = file.get(current);
        final short head =  (short) (rowid & Storage.OFFSET_MASK);

        // allocate a return buffer
        // byte[] retval = new byte[ head.getCurrentSize() ];
        final int size = EntryHeader.getCurrentSize(page, head);
        if (size == 0) {
            file.release(current, false);
            return;
        }

        // copy bytes in
        int leftToRead = size;
        short dataOffset = (short) ( head + EntryHeader.SIZE);
        while (leftToRead > 0) {
            // copy current page's data to return buffer
            int toCopy = PAGE_SIZE - dataOffset;
            if (leftToRead < toCopy) {
                toCopy = leftToRead;
            }

            out.writeFromByteBuffer(page.getData(), dataOffset, toCopy);

            // Go to the next page
            leftToRead -= toCopy;
            // out.flush();
            file.release(page);

            if (leftToRead > 0) {
                current = pageman.getNext(current);
                page = file.get(current);
                dataOffset = Const.DATA_PAGE_O_DATA;
            }

        }

        // return retval;
    }

    /**
     * Allocate a new rowid with the indicated size.
     */
    private long alloc(int size) throws IOException {
        size = EntryHeader.roundAvailableSize(size);
        long retval = freeman.getFreeRecord(size);
        if (retval == 0) {
            retval = allocNew(size, pageman.getLast(Const.USED_PAGE));
        }
        return retval;
    }

    /**
     * Allocates a new rowid. The second parameter is there to allow for a recursive call - it indicates where the
     * search should start.
     */
    private long allocNew(int size, long start) throws IOException {
        PageIo curPage;
        if (start == 0 ||
                //last page was completely filled?
                cachedLastAllocatedRecordPage == start && cachedLastAllocatedRecordOffset == PAGE_SIZE
                ) {
            // we need to create a new page.
            start = pageman.allocate(Const.USED_PAGE);
            curPage = file.get(start);
            curPage.dataPageSetFirst(Const.DATA_PAGE_O_DATA);
            cachedLastAllocatedRecordOffset = Const.DATA_PAGE_O_DATA;
            cachedLastAllocatedRecordPage = curPage.getPageId();
            EntryHeader.setAvailableSize(curPage, Const.DATA_PAGE_O_DATA, 0);
            EntryHeader.setCurrentSize(curPage, Const.DATA_PAGE_O_DATA, 0);

        } else {
            curPage = file.get(start);
        }

        // follow the rowids on this page to get to the last one. We don't
        // fall off, because this is the last page, remember?
        short pos = curPage.dataPageGetFirst();
        if (pos == 0) {
            // page is exactly filled by the last page of a record
            file.release(curPage);
            return allocNew(size, 0);
        }

        short hdr = pos;

        if (cachedLastAllocatedRecordPage != curPage.getPageId() ) {
            //position was not cached, have to find it again
            int availSize = EntryHeader.getAvailableSize(curPage, hdr);
            while (availSize != 0 && pos < PAGE_SIZE) {
                pos += availSize + EntryHeader.SIZE;
                if (pos == PAGE_SIZE) {
                    // Again, a filled page.
                    file.release(curPage);
                    return allocNew(size, 0);
                }
                hdr = pos;
                availSize = EntryHeader.getAvailableSize(curPage, hdr);
            }
        } else {
            hdr = cachedLastAllocatedRecordOffset;
            pos = cachedLastAllocatedRecordOffset;
        }


        if (pos == EntryHeader.SIZE) { //TODO why is this here?
            // the last record exactly filled the page. Restart forcing
            // a new page.
            file.release(curPage);
        }
        
        if(hdr>Storage.PAGE_SIZE - 16){
            file.release(curPage);
            //there is not enought space on current page, so force new page
            return allocNew(size,0);
        }

        // we have the position, now tack on extra pages until we've got
        // enough space.
        long retval =(start << Storage.PAGE_SIZE_SHIFT) + (long) pos;
        int freeHere = PAGE_SIZE - pos - EntryHeader.SIZE;
        if (freeHere < size) {
            // check whether the last page would have only a small bit left.
            // if yes, increase the allocation. A small bit is a record
            // header plus 16 bytes.
            int lastSize = (size - freeHere) % DATA_PER_PAGE;
            if (size <DATA_PER_PAGE && (DATA_PER_PAGE - lastSize) < (EntryHeader.SIZE + 16)) {
                size += (DATA_PER_PAGE - lastSize);
                size = EntryHeader.roundAvailableSize(size);
            }

            // write out the header now so we don't have to come back.
            EntryHeader.setAvailableSize(curPage, hdr, size);
            file.release(start, true);

            int neededLeft = size - freeHere;
            // Refactor these two pages!
            while (neededLeft >= DATA_PER_PAGE) {
                start = pageman.allocate(Const.USED_PAGE);
                curPage = file.get(start);
                curPage.dataPageSetFirst((short) 0); // no rowids, just data
                file.release(start, true);
                neededLeft -= DATA_PER_PAGE;
            }
            if (neededLeft > 0) {
                // done with whole chunks, allocate last fragment.
                start = pageman.allocate(Const.USED_PAGE);
                curPage = file.get(start);
                curPage.dataPageSetFirst((short) (Const.DATA_PAGE_O_DATA + neededLeft));
                file.release(start, true);
                cachedLastAllocatedRecordOffset = (short) (Const.DATA_PAGE_O_DATA + neededLeft);
                cachedLastAllocatedRecordPage = curPage.getPageId();

            }
        } else {
            // just update the current page. If there's less than 16 bytes
            // left, we increase the allocation (16 bytes is an arbitrary
            // number).
            if (freeHere - size <= (16 + EntryHeader.SIZE)) {
                size = freeHere;
            }
            EntryHeader.setAvailableSize(curPage, hdr, size);
            file.release(start, true);
            cachedLastAllocatedRecordOffset = (short) (hdr + EntryHeader.SIZE + size);
            cachedLastAllocatedRecordPage = curPage.getPageId();

        }
        return retval;

    }

    void free(final long id) throws IOException {
        // get the rowid, and write a zero current size into it.
        final long curPageId = id >>> Storage.PAGE_SIZE_SHIFT;
        final PageIo curPage = file.get(curPageId);
        final short offset =  (short) (id & Storage.OFFSET_MASK);
        EntryHeader.setCurrentSize(curPage, offset, 0);
        int size = EntryHeader.getAvailableSize(curPage, offset);

        //trim size if spreads across multiple pages
        if(offset + EntryHeader.SIZE + size >PAGE_SIZE + (PAGE_SIZE-Const.DATA_PAGE_O_DATA)){
            int numOfPagesToSkip = (size -
                    (Storage.PAGE_SIZE-(offset - EntryHeader.SIZE))  //minus data remaining on this page
                    )/(PAGE_SIZE-Const.DATA_PAGE_O_DATA);
            size = size - numOfPagesToSkip * (PAGE_SIZE-Const.DATA_PAGE_O_DATA);
            EntryHeader.setAvailableSize(curPage, offset,size);
            
            //get next page 
            long nextPage = curPage.pageHeaderGetNext();
            file.release(curPage);


            //release pages
            for(int i = 0;i<numOfPagesToSkip;i++){
                PageIo page = file.get(nextPage);
                long nextPage2 = page.pageHeaderGetNext();
                file.release(page);
                pageman.free(Const.USED_PAGE,nextPage);
                nextPage = nextPage2;
            }

        }else{
            file.release(curPage);
        }
        




        // write the rowid to the free list
        freeman.putFreeRecord(id, size);
    }

    /**
     * Writes out data to a rowid. Assumes that any resizing has been done.
     */
    private void write(final long rowid, final  byte[] data,final  int start, final  int length) throws IOException {
        long current =  rowid >>> Storage.PAGE_SIZE_SHIFT;
        PageIo page = file.get(current);
        final short hdr =  (short) (rowid & Storage.OFFSET_MASK);
        EntryHeader.setCurrentSize(page, hdr, length);
        if (length == 0) {
            file.release(current, true);
            return;
        }

        // copy bytes in
        int offsetInBuffer = start;
        int leftToWrite = length;
        short dataOffset = (short) (hdr + EntryHeader.SIZE);
        while (leftToWrite > 0) {
            // copy current page's data to return buffer
            int toCopy = PAGE_SIZE - dataOffset;

            if (leftToWrite < toCopy) {
                toCopy = leftToWrite;
            }
            page.writeByteArray(data, offsetInBuffer, dataOffset, toCopy);

            // Go to the next page
            leftToWrite -= toCopy;
            offsetInBuffer += toCopy;

            file.release(current, true);

            if (leftToWrite > 0) {
                current = pageman.getNext(current);
                page = file.get(current);
                dataOffset = Const.DATA_PAGE_O_DATA;
            }
        }
    }

    void rollback() throws IOException {
        cachedLastAllocatedRecordPage = Long.MIN_VALUE;
        cachedLastAllocatedRecordOffset = Short.MIN_VALUE;
        freeman.rollback();
    }


    void commit() throws IOException {
        freeman.commit();
    }
}
