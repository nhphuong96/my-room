package com.myroom.core;

import com.myroom.R;

public final class GenderFactory {
    public static final Integer MALE = 1;
    public static final Integer FEMALE = 0;

    public static int getGenderAsInt(int resId) {
        if (resId == R.id.radioButton_male) {
            return MALE;
        }
        return FEMALE;
    }

}
