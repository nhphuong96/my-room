package com.myroom.service.sdo;

public class IndexPair {
    private String lastIndex;
    private String currentIndex;

    public IndexPair() {
    }

    public IndexPair(String lastIndex, String currentIndex) {
        this.lastIndex = lastIndex;
        this.currentIndex = currentIndex;
    }

    public String getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(String lastIndex) {
        this.lastIndex = lastIndex;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }
}
