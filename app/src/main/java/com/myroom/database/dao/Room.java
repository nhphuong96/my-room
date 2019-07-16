package com.myroom.database.dao;

public class Room {
    public static final String TABLE_NAME = "room";
    public enum Column {
        COLUMN_ROOM_KEY(0, "room_key"),
        COLUMN_ROOM_NAME(1, "room_name");

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
    private String roomName;

    public Room() {
    }

    public long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(long roomKey) {
        this.roomKey = roomKey;
    }

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
