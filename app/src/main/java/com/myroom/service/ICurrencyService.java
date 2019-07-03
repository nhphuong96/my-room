package com.myroom.service;

import com.myroom.service.sdo.ReadAvailableCurrencyOut;

public interface ICurrencyService {
    ReadAvailableCurrencyOut readAvailableCurrency();
    void updateSelectedCurrency(long id);
}
