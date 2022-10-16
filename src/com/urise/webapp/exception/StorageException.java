package com.urise.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    protected StorageException(String uuid) {
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception cause) {
        super(message, cause);
        this.uuid = null;
    }

    public StorageException(String message, String uuid, Exception cause) {
        super(message, cause);
        this.uuid = uuid;
    }
}
