package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.SendMessageIn;

public interface IMessageService {
    void sendMessage(SendMessageIn sendMessageIn) throws ValidationException, OperationException;
}
