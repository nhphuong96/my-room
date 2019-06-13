package com.myroom.core;

import com.myroom.exception.ValidationException;

import org.apache.commons.lang3.StringUtils;

public final class Assert {

    private Assert() {

    }

    public static <T extends Object> void assertNotNull(T value, String errorMessage) throws ValidationException {
        if (value == null) {
            throw new ValidationException(errorMessage);
        }
    }
}
