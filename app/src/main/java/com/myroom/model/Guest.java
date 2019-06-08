package com.myroom.model;

public class Guest extends BaseModel {
    public static final String TABLE_NAME = "guest";
    public enum Column {
        COLUMN_GUEST_NAME(1, "guest_name"),
        COLUMN_BIRTH_DATE(2, "birth_date"),
        COLUMN_ID_CARD(3, "id_card"),
        COLUMN_PHONE_NUMBER(4, "phone_number"),
        COLUMN_ROOM_ID(5, "room_id");

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

    private String guestName;
    private String birthDate;
    private String idCard;
    private String phoneNumber;
    private long roomId;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestName='" + guestName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
