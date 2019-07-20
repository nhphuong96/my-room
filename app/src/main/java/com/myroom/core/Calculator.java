package com.myroom.core;

public final class Calculator {
    private Calculator() {

    }

    public static Double calculate(Integer currentIndex, Integer lastIndex, Double fee) {
        int consume = currentIndex - lastIndex;
        return fee * consume;
    }

    public static Double calculate(Integer index, Double fee) {
        return index * fee;
    }
}
