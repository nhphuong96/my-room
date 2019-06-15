package com.myroom.service.sdo;

import com.myroom.database.dao.Utility;

import java.util.Map;

public class ReadAvailableUtilityOut {
    private Map<Utility, Double> utilityList;

    public Map<Utility, Double> getUtilityList() {
        return utilityList;
    }

    public void setUtilityList(Map<Utility, Double> utilityList) {
        this.utilityList = utilityList;
    }
}
