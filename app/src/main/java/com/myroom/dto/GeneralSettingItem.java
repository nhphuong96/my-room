package com.myroom.dto;

import com.myroom.core.GeneralSettingId;

public class GeneralSettingItem {
    private GeneralSettingId id;
    private String name;
    private Integer imageRes;

    public GeneralSettingItem(GeneralSettingId id, String name, Integer imgRes) {
        this.id = id;
        this.name = name;
        this.imageRes = imgRes;
    }

    public GeneralSettingId getId() {
        return id;
    }

    public void setId(GeneralSettingId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImageRes() {
        return imageRes;
    }

    public void setImageRes(Integer imageRes) {
        this.imageRes = imageRes;
    }
}
