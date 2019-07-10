package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.core.Constant;
import com.myroom.database.dao.Currency;
import com.myroom.database.repository.CurrencyRepository;
import com.myroom.exception.OperationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.sdo.ReadAvailableCurrencyOut;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CurrencyServiceImpl implements ICurrencyService {

    private static List<Currency> availableCurrencyList;

    @Inject public CurrencyRepository currencyRepository;

    @Inject
    public CurrencyServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Inject
    public void posConstruct() {
        initializeAvailableCurrency();
    }

    private void initializeAvailableCurrency() {
        List<Currency> currencyList = currencyRepository.findAll();
        availableCurrencyList = new ArrayList<>(currencyList);
    }

    @Override
    public ReadSelectedCurrencyOut readSelectedCurrency() throws OperationException {
        ReadSelectedCurrencyOut readSelectedCurrencyOut = new ReadSelectedCurrencyOut();
        for (Currency currency : availableCurrencyList) {
            if (currency.getIsSelected() == Constant.TRUE) {
                readSelectedCurrencyOut.setCurrency(currency);
                break;
            }
        }
        if (readSelectedCurrencyOut.getCurrency() == null) {
            throw new OperationException("Lỗi xảy ra: Không tìm thấy tiền tệ mặc định.");
        }
        return readSelectedCurrencyOut;
    }

    @Override
    public ReadAvailableCurrencyOut readAvailableCurrency() {
        ReadAvailableCurrencyOut readAvailableCurrencyOut = new ReadAvailableCurrencyOut();
        readAvailableCurrencyOut.setCurrencyList(availableCurrencyList);
        return readAvailableCurrencyOut;
    }

    @Override
    public void updateSelectedCurrency(long id) {
        for (Currency currency : availableCurrencyList) {
            if (currency.getId() == id) {
                currency.setIsSelected(Constant.TRUE);
                //Update database
                Currency selectedCurrency = currencyRepository.find(id);
                selectedCurrency.setIsSelected(Constant.TRUE);
                currencyRepository.update(selectedCurrency);
            }
            else {
                if (currency.getIsSelected() == Constant.TRUE)
                {
                    Currency unselectCurrency = currencyRepository.find(currency.getId());
                    unselectCurrency.setIsSelected(Constant.FALSE);
                    currencyRepository.update(unselectCurrency);
                }
                currency.setIsSelected(Constant.FALSE);
            }
        }
    }
}
