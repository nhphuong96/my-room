package com.myroom.service.sdo;

import java.util.List;

public class CreateRoomIn {
    private String roomName;
    private String guestName;
    private String guestBirthDate;
    private String guestIdCard;
    private String guestPhoneNumber;
    private int gender;
    private List<Long> utilityKeyList;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Long> getUtilityKeyList() {
        return utilityKeyList;
    }

    public void setUtilityKeyList(List<Long> utilityKeyList) {
        this.utilityKeyList = utilityKeyList;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestBirthDate() {
        return guestBirthDate;
    }

    public void setGuestBirthDate(String guestBirthDate) {
        this.guestBirthDate = guestBirthDate;
    }

    public String getGuestIdCard() {
        return guestIdCard;
    }

    public void setGuestIdCard(String guestIdCard) {
        this.guestIdCard = guestIdCard;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
