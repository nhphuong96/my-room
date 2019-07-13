package com.myroom.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {

    }

    public static String convertDateToStringAsDDMMYYYYHHMMSS(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
            return sdf.format(date);
        }
        catch (Exception e) {
            //NOP
        }
        return null;
    }

    public static Date convertStringToDateAsDDMMYYYYHHMMSS(String dateAsString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            return sdf.parse(dateAsString);
        }
        catch (Exception e) {
            //NOP
        }
        return null;
    }

    public static String convertDateToStringAsDDMMYYYY(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            return sdf.format(date);
        }
        catch (Exception e) {
            //NOP
        }
        return null;
    }

}
