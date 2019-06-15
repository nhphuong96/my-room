package com.myroom.dto;

public class UtilityInRoomItem {
    private long utilityId;
    private String utilityName;
    private Double utilityFee;

    public long getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(long utilityId) {
        this.utilityId = utilityId;
    }

    public String getUtilityName() {
        return utilityName;
    }

    public void setUtilityName(String utilityName) {
        this.utilityName = utilityName;
    }

    public Double getUtilityFee() {
        return utilityFee;
    }

    public void setUtilityFee(Double utilityFee) {
        this.utilityFee = utilityFee;
    }
}
