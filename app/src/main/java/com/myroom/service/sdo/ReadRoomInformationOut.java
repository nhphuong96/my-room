package com.myroom.service.sdo;

import java.util.List;

public class ReadRoomInformationOut {
    private List<RoomInfo> roomInfoList;

    public List<RoomInfo> getRoomInfoList() {
        return roomInfoList;
    }

    public void setRoomInfoList(List<RoomInfo> roomInfoList) {
        this.roomInfoList = roomInfoList;
    }
}
