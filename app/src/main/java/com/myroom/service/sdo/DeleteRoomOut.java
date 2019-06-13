package com.myroom.service.sdo;

import java.util.ArrayList;
import java.util.List;

public class DeleteRoomOut {
    private List<Long> afftectedRoomId;

    public DeleteRoomOut() {
        afftectedRoomId = new ArrayList<>();
    }

    public List<Long> getAfftectedRoomId() {
        return afftectedRoomId;
    }

    public void setAfftectedRoomId(List<Long> afftectedRoomId) {
        this.afftectedRoomId = afftectedRoomId;
    }
}
