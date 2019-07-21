package com.myroom.database.dao;

public class UtilityIndex {
    public static final String TABLE_NAME = "utility_index";
    public enum Column {
        COLUMN_UTILITY_INDEX_KEY(0, "utility_index_key"),
        COLUMN_PAYMENT_KEY(1, "payment_key"),
        COLUMN_UTILITY_KEY(2, "utility_key"),
        COLUMN_LAST_INDEX(3, "last_index"),
        COLUMN_CURRENT_INDEX(4, "current_index");

        private int index;
        private String colName;

        Column(int index, String colName) {
            this.index = index;
            this.colName = colName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }
    }

    private long utilityIndexKey;
    private long paymentKey;
    private long utilityKey;
    private String lastIndex;
    private String currentIndex;

    public long getUtilityIndexKey() {
        return utilityIndexKey;
    }

    public void setUtilityIndexKey(long utilityIndexKey) {
        this.utilityIndexKey = utilityIndexKey;
    }

    public long getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(long paymentKey) {
        this.paymentKey = paymentKey;
    }

    public long getUtilityKey() {
        return utilityKey;
    }

    public void setUtilityKey(long utilityKey) {
        this.utilityKey = utilityKey;
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
