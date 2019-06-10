package com.myroom.model;

public class Utility extends BaseModel {
    public static final String TABLE_NAME = "utility";
    public enum Column {
        COLUMN_UTILITY_NAME(1, "utility_name");

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

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
