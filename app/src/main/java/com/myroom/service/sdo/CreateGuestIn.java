package com.myroom.service.sdo;

public class CreateGuestIn {
    private long roomKey;
    private String guestName;
    private String guestPhone;
    private String guestIdCard;
    private String guestBirthday;
    private int gender;

    public long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(long roomKey) {
        this.roomKey = roomKey;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGuestIdCard() {
        return guestIdCard;
    }

    public void setGuestIdCard(String guestIdCard) {
        this.guestIdCard = guestIdCard;
    }

    public String getGuestBirthday() {
        return guestBirthday;
    }

    public void setGuestBirthday(String guestBirthday) {
        this.guestBirthday = guestBirthday;
    }
}
