package com.urise.webapp.exception;

public class ExistStorageException extends StorageException{
    public ExistStorageException(String uuid) {
        super("Can't store resume uuid = " + uuid + " because it already exists", uuid);
    }
}
