package com.myroom.database.dao;

import java.util.Date;

public class Payment extends BaseModel {

    public static final String TABLE_NAME = "payment";
    public enum Column {
        COLUMN_ROOM_ID(1, "room_id"),
        COLUMN_CREATION_DATE(2, "creation_date"),
        COLUMN_PAYMENT_DATE(3, "payment_date"),
        COLUMN_ELECTRICITY_FEE(4, "electricity_fee"),
        COLUMN_WATER_FEE(5, "water_fee"),
        COLUMN_CAB_FEE(6, "cab_fee"),
        COLUMN_INTERNET_FEE(7, "internet_fee"),
        COLUMN_ROOM_FEE(8, "room_fee");

        private int index;
        private String colName;

        Column(int index, String colName) {
            this.index = index;
            this.colName = colName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }
    }

    private long roomId;
    private Date creationDate;
    private Date paymentDate;
    private String electricityFee;
    private String waterFee;
    private String cabFee;
    private String internetFee;
    private String roomFee;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(String electricityFee) {
        this.electricityFee = electricityFee;
    }

    public String getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(String waterFee) {
        this.waterFee = waterFee;
    }

    public String getCabFee() {
        return cabFee;
    }

    public void setCabFee(String cabFee) {
        this.cabFee = cabFee;
    }

    public String getInternetFee() {
        return internetFee;
    }

    public void setInternetFee(String internetFee) {
        this.internetFee = internetFee;
    }

    public String getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(String roomFee) {
        this.roomFee = roomFee;
    }
}
