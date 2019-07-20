package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.CreatePaymentIn;
import com.myroom.service.sdo.DeletePaymentIn;

public interface IPaymentService {
    void createPayment(CreatePaymentIn createPaymentIn) throws ValidationException, OperationException;
    void deletePayment(DeletePaymentIn deletePaymentIn) throws ValidationException, OperationException;
}
