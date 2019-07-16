package com.myroom.database.dao;

public class Utility {
    public static final String TABLE_NAME = "utility";
    public enum Column {
        COLUMN_UTILITY_KEY(0, "utility_key"),
        COLUMN_UTILITY_ID(1, "utility_id"),
        COLUMN_UTILITY_NAME(2, "utility_name"),
        COLUMN_UTILITY_ICON(3, "utility_icon");

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

    private long utilityKey;
    private String utilityId;
    private String name;
    private String icon;

    public long getUtilityKey() {
        return utilityKey;
    }

    public void setUtilityKey(long utilityKey) {
        this.utilityKey = utilityKey;
    }

    public String getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(String utilityId) {
        this.utilityId = utilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
