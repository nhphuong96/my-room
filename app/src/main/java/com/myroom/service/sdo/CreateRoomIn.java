package com.myroom.service.sdo;

import java.util.List;

public class CreateRoomIn {
    private String roomName;
    private String guestName;
    private String guestBirthDate;
    private String guestIdCard;
    private String guestPhoneNumber;
    private List<Long> utilityIdList;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Long> getUtilityIdList() {
        return utilityIdList;
    }

    public void setUtilityIdList(List<Long> utilityIdList) {
        this.utilityIdList = utilityIdList;
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
}
