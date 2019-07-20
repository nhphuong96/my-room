package com.myroom.service.sdo;

public class CreateUtilityInRoomIn {
    private long roomKey;
    private long utilityKey;
    private String fee;

    public long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(long roomKey) {
        this.roomKey = roomKey;
    }

    public long getUtilityKey() {
        return utilityKey;
    }

    public void setUtilityKey(long utilityKey) {
        this.utilityKey = utilityKey;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
