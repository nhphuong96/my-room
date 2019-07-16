package com.myroom.database.dao;

public class RoomUtility {
    public static final String TABLE_NAME = "room_utility";
    public enum Column {
        COLUMN_ROOM_KEY(0, "room_key"),
        COLUMN_UTILITY_KEY(1, "utility_key"),
        COLUMN_UTILITY_FEE(2, "utility_fee");

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

    private long roomKey;
    private long utilityKey;
    private String utilityFee;

    public long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(long roomKey) {
        this.roomKey = roomKey;
    }

    public long getUtilityKey() {
        return utilityKey;
    }

    public void setUtilityKey(long utilityKey) {
        this.utilityKey = utilityKey;
    }

    public String getUtilityFee() {
        return utilityFee;
    }

    public void setUtilityFee(String utilityFee) {
        this.utilityFee = utilityFee;
    }

}
