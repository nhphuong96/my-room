package com.myroom.service.sdo;

import com.myroom.database.dao.Currency;

import java.util.List;

public class ReadAvailableCurrencyOut {
    private List<Currency> currencyList;

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
}
