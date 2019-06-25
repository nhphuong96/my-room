package com.myroom.core;

public final class NumberFormatter {
    private NumberFormatter() {

    }

    public static String formatThousandNumberSeparator(Integer value) {
        if (value != null) {
            return String.format("%,d", value);
        }
        return null;
    }
}
