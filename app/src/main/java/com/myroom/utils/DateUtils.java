package com.myroom.utils;

import com.myroom.core.Constant;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.myroom.core.Constant.DATE_FORMAT_WITH_TIME;

public final class DateUtils {

    private DateUtils() {

    }

    public static String convertDateToStringAsDDMMYYYYHHMMSS(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITH_TIME);
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
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITH_TIME);
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
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_STANDARD);
                return sdf.format(date);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return null;
    }

}
