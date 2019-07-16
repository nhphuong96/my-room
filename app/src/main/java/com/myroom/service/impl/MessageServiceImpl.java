package com.myroom.service.impl;

import android.telephony.SmsManager;

import com.myroom.builder.MessageBuilder;
import com.myroom.core.Assert;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IMessageService;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.CreateMessageIn;
import com.myroom.service.sdo.CreateMessageOut;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;
import com.myroom.service.sdo.SendMessageIn;

import javax.inject.Inject;

public class MessageServiceImpl implements IMessageService {

    private static SmsManager smsManager = SmsManager.getDefault();

    @Inject
    public IRoomService roomService;
    @Inject
    public ICurrencyService currencyService;

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

    @Override
    public CreateMessageOut createMessage(CreateMessageIn createMessageIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createMessageIn, "createMessageIn must not be null.");
        Assert.assertNotNull(createMessageIn.getRoomKey(), "createMessageIn.roomKey must not be null.");

        ReadAvailableUtilityOut readAvailableUtilityOut = roomService.readAvailableUtility(convertReadAvailableUtilityIn(createMessageIn));
        ReadSelectedCurrencyOut readSelectedCurrencyOut = currencyService.readSelectedCurrency();
        MessageBuilder messageBuilder = new MessageBuilder();
        String messageContent = messageBuilder.setCreateMessageIn(createMessageIn)
                .setReadAvailableUtilityOut(readAvailableUtilityOut)
                .setSelectedCurrency(readSelectedCurrencyOut.getCurrency())
                .build();
        CreateMessageOut createMessageOut = new CreateMessageOut();
        createMessageOut.setMessageContent(messageContent);
        return createMessageOut;
    }

    private ReadAvailableUtilityIn convertReadAvailableUtilityIn(CreateMessageIn createMessageIn) {
        ReadAvailableUtilityIn readAvailableUtilityIn = new ReadAvailableUtilityIn();
        readAvailableUtilityIn.setRoomKey(createMessageIn.getRoomKey());
        return readAvailableUtilityIn;
    }

}
