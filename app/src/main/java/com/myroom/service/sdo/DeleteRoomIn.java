package com.myroom.service.sdo;

import com.myroom.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DeleteRoomIn {
    private List<Long> roomIdList;

    public DeleteRoomIn() {
        roomIdList = new ArrayList<>();
    }

    public List<Long> getRoomIdList() {
        return roomIdList;
    }

    public void setRoomIdList(List<Long> roomIdList) {
        this.roomIdList = roomIdList;
    }
}
