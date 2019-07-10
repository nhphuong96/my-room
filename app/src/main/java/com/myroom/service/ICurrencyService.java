package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.ReadAvailableCurrencyOut;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;

public interface ICurrencyService {
    ReadSelectedCurrencyOut readSelectedCurrency() throws OperationException;
    ReadAvailableCurrencyOut readAvailableCurrency() throws OperationException;
    void updateSelectedCurrency(long id) throws ValidationException, OperationException;
}
