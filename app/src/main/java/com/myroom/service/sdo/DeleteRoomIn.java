package com.myroom.service.sdo;

import java.util.ArrayList;
import java.util.List;

public class DeleteRoomIn {
    private List<Long> roomKeyList;

    public DeleteRoomIn() {
        this.roomKeyList = new ArrayList<>();
    }

    public List<Long> getRoomKeyList() {
        return roomKeyList;
    }

    public void setRoomKeyList(List<Long> roomKeyList) {
        this.roomKeyList = roomKeyList;
    }
}
