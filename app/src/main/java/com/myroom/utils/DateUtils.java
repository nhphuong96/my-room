package com.myroom.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {

    }

    public static String convertDateToStringAsDDMMYYYYHHMMSS(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                return sdf.format(date);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return null;
    }

    public static Date convertStringToDateAsDDMMYYYYHHMMSS(String dateAsString) {
        if (StringUtils.isNotEmpty(dateAsString)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat();
                return sdf.parse(dateAsString);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return null;
    }

    public static String convertDateToStringAsDDMMYYYY(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(date);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return null;
    }

}
