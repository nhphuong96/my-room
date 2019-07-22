package com.myroom.utils;

public final class FormattedNumberUtils {
    private FormattedNumberUtils() {

    }

    public static String formatThousandNumberSeparator(String value) {
        if (value != null) {
            if (value.contains(".")) {
                String[] splittedNumber = value.split("\\.");
                if (splittedNumber[1].equals("0")) {
                    return String.format("%,d", Integer.valueOf(splittedNumber[0]));
                }
                return String.format("%,d", Integer.valueOf(splittedNumber[0])) + "." + splittedNumber[1];
            }
            return String.format("%,d", Integer.valueOf(value));
        }
        return null;
    }

}
