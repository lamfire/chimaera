package com.lamfire.chimaera.store.bdbstore;

public class BDBStorageException extends Exception {

	private static final long serialVersionUID = -2069977285519636958L;

	public BDBStorageException(String message) {
		super(message);
	}

	public BDBStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
