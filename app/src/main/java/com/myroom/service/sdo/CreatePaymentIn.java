package com.myroom.service.sdo;

public class CreatePaymentIn {
    private long roomKey;
    private IndexPair electricityIndices;
    private Integer waterIndex;
    private Integer cabIndex;
    private Integer internetIndex;
    private Integer roomIndex;

    public long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(long roomKey) {
        this.roomKey = roomKey;
    }

    public IndexPair getElectricityIndices() {
        return electricityIndices;
    }

    public void setElectricityIndices(IndexPair electricityIndices) {
        this.electricityIndices = electricityIndices;
    }

    public Integer getWaterIndex() {
        return waterIndex;
    }

    public void setWaterIndex(Integer waterIndex) {
        this.waterIndex = waterIndex;
    }

    public Integer getCabIndex() {
        return cabIndex;
    }

    public void setCabIndex(Integer cabIndex) {
        this.cabIndex = cabIndex;
    }

    public Integer getInternetIndex() {
        return internetIndex;
    }

    public void setInternetIndex(Integer internetIndex) {
        this.internetIndex = internetIndex;
    }

    public Integer getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(Integer roomIndex) {
        this.roomIndex = roomIndex;
    }
}
