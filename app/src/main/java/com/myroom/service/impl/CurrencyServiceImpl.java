package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Currency;
import com.myroom.database.repository.CurrencyRepository;
import com.myroom.service.ICurrencyService;
import com.myroom.service.sdo.ReadAvailableCurrencyOut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CurrencyServiceImpl implements ICurrencyService {

    private List<Currency> availableCurrencyList;

    @Inject public CurrencyRepository currencyRepository;

    @Inject
    public CurrencyServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public ReadAvailableCurrencyOut readAvailableCurrency() {
        ReadAvailableCurrencyOut readAvailableCurrencyOut = new ReadAvailableCurrencyOut();
        if (availableCurrencyList == null) {
            List<Currency> currencyList = currencyRepository.findAll();
            availableCurrencyList = new ArrayList<>(currencyList);
            availableCurrencyList.get(0).setSelected(true);
        }
        readAvailableCurrencyOut.setCurrencyList(availableCurrencyList);
        return readAvailableCurrencyOut;
    }

    @Override
    public void updateSelectedCurrency(long id) {
        for (Currency currency : availableCurrencyList) {
            if (currency.getId() == id) {
                currency.setSelected(true);
            }
            else {
                currency.setSelected(false);
            }
        }
    }
}
