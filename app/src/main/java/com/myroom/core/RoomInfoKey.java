package com.myroom.core;

public enum  RoomInfoKey {

    ROOM_NAME("Tên phòng");

    private String denotation;

    RoomInfoKey(String denotation) {
        this.denotation = denotation;
    }

    public String getDenotation() {
        return denotation;
    }

}
