package com.myroom.database.dao;

public class Guest {
    public static final String TABLE_NAME = "guest";
    public enum Column {
        COLUMN_GUEST_KEY(0, "guest_key"),
        COLUMN_GUEST_NAME(1, "guest_name"),
        COLUMN_BIRTH_DATE(2, "birth_date"),
        COLUMN_ID_CARD(3, "id_card"),
        COLUMN_PHONE_NUMBER(4, "phone_number"),
        COLUMN_ROOM_ID(5, "room_id"),
        COLUMN_GENDER(6, "gender");

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

    private long guestKey;
    private String guestName;
    private String birthDate;
    private String idCard;
    private String phoneNumber;
    private long roomId;
    private int gender;

    public Guest() {
    }

    public Guest(String guestName, String birthDate, String idCard, String phoneNumber, long roomId, int gender) {
        this.guestName = guestName;
        this.birthDate = birthDate;
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.roomId = roomId;
        this.gender = gender;
    }

    public long getGuestKey() {
        return guestKey;
    }

    public void setGuestKey(long guestKey) {
        this.guestKey = guestKey;
    }

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestName='" + guestName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roomId=" + roomId +
                ", gender=" + gender +
                '}';
    }
}
