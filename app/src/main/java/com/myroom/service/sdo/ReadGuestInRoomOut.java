package com.myroom.service.sdo;

import com.myroom.database.dao.Guest;

import java.util.List;

public class ReadGuestInRoomOut {
    private List<Guest> guestList;

    public List<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }
}
