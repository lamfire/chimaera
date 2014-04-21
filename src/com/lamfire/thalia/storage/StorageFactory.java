package com.lamfire.thalia.storage;

import java.io.IOException;

import com.lamfire.thalia.ThaliaBuilder;

public class StorageFactory {
	
	public static Storage makeStorageMemory(boolean transactionsDisabled)throws IOException{
		return new StorageMemory(transactionsDisabled);
	}
	
	public static Storage makeStorageZip(String fileName)throws IOException{
		return new StorageZip(ThaliaBuilder.isZipFileLocation(fileName));
	}
	
	public static Storage makeStorageDisk(String fileName,boolean readonly,boolean lockingDisabled)throws IOException{
		return new StorageDisk(fileName,readonly,lockingDisabled);
	}
	
	public static Storage makeStorageDiskMapped(String fileName,boolean readonly,boolean transactionsDisabled,boolean lockingDisabled)throws IOException{
		return new StorageDiskMapped(fileName,readonly,transactionsDisabled,lockingDisabled);
	}
}
