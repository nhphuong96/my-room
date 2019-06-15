package com.myroom.service.sdo;

import com.myroom.dto.UtilityInRoomItem;

import java.util.List;

public class ReadAvailableUtilityOut {
    private List<UtilityInRoomItem> utilityInRoomItemList;

    public List<UtilityInRoomItem> getUtilityInRoomItemList() {
        return utilityInRoomItemList;
    }

    public void setUtilityInRoomItemList(List<UtilityInRoomItem> utilityInRoomItemList) {
        this.utilityInRoomItemList = utilityInRoomItemList;
    }
}
