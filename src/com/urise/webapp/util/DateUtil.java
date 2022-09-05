package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;

final public class DateUtil {
    private DateUtil() {
    }

    public static LocalDate of (int year, Month month) {
        return LocalDate.of(year, month, 1);
    }
}
