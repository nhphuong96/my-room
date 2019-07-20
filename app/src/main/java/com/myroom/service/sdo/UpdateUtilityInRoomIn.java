package com.myroom.service.sdo;

public class UpdateUtilityInRoomIn {
    private long roomKey;
    private long utilityKey;
    private String utilityFee;

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

    public String getUtilityFee() {
        return utilityFee;
    }

    public void setUtilityFee(String utilityFee) {
        this.utilityFee = utilityFee;
    }
}
