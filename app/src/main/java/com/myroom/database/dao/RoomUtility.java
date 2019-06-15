package com.myroom.database.dao;

public class RoomUtility {
    public static final String TABLE_NAME = "room_utility";
    public enum Column {
        COLUMN_ROOM_ID(0, "room_id"),
        COLUMN_UTILITY_ID(1, "utility_id"),
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

    private long roomId;
    private long utilityId;
    private double utilityFee;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(long utilityId) {
        this.utilityId = utilityId;
    }

    public double getUtilityFee() {
        return utilityFee;
    }

    public void setUtilityFee(double utilityFee) {
        this.utilityFee = utilityFee;
    }

    @Override
    public String toString() {
        return "RoomUtilityRepository{" +
                "roomId=" + roomId +
                ", utilityId=" + utilityId +
                ", utilityFee=" + utilityFee +
                '}';
    }
}
