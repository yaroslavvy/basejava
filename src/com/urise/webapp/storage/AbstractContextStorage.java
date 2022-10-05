package com.urise.webapp.storage;

import java.util.Objects;

abstract public class AbstractContextStorage<SK> extends AbstractStorage<SK> {

    private FormatStrategy formatStrategy;

    protected AbstractContextStorage(FormatStrategy formatStrategy) {
        setFormatStrategy(formatStrategy);
    }

    public void setFormatStrategy(FormatStrategy formatStrategy) {
        Objects.requireNonNull(formatStrategy, "Format strategy for storage must not be null!");
        this.formatStrategy = formatStrategy;
    }

    public FormatStrategy getFormatStrategy() {
        return formatStrategy;
    }
}
