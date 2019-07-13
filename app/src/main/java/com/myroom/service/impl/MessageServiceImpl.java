package com.myroom.service.impl;

import android.telephony.SmsManager;

import com.myroom.core.Assert;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IMessageService;
import com.myroom.service.sdo.SendMessageIn;

import javax.inject.Inject;

public class MessageServiceImpl implements IMessageService {

    private static SmsManager smsManager = SmsManager.getDefault();

    @Inject
    public MessageServiceImpl() {
    }

    @Override
    public void sendMessage(SendMessageIn sendMessageIn) throws ValidationException, OperationException {
        Assert.assertNotNull(sendMessageIn, "sendMessageIn must not be null.");
        Assert.assertNotNull(sendMessageIn.getMessageContent(), "sendMessageIn.messageContent must not be null.");
        Assert.assertNotNull(sendMessageIn.getRecipient(), "sendMessageIn.recipient must not be null.");
        try {
            smsManager.sendTextMessage(sendMessageIn.getRecipient(), null, sendMessageIn.getMessageContent(), null, null);
        }
        catch (Exception e) {
            throw new OperationException(e.getMessage());
        }

    }
}
