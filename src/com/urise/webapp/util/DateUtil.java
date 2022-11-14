package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;

final public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(4000, 1, 1);

    private DateUtil() {
    }

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }
}
