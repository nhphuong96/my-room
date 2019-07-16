package com.myroom.service.sdo;

public class IndexPair {
    private Integer lastIndex;
    private Integer currentIndex;

    public IndexPair(Integer lastIndex, Integer currentIndex) {
        this.lastIndex = lastIndex;
        this.currentIndex = currentIndex;
    }

    public Integer getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(Integer lastIndex) {
        this.lastIndex = lastIndex;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }
}
