package com.myroom.application;

import com.myroom.activity.CreateRoomActivity;
import com.myroom.adapter.CurrencyAdapter;
import com.myroom.adapter.GuestInRoomAdapter;
import com.myroom.adapter.RoomAdapter;
import com.myroom.adapter.RoomInformationAdapter;
import com.myroom.adapter.UtilityInRoomAdapter;
import com.myroom.fragment.CreateBillFragment;
import com.myroom.fragment.GuestInRoomFragment;
import com.myroom.fragment.SendMessageFragment;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IGuestService;
import com.myroom.service.IMessageService;
import com.myroom.service.IPaymentService;
import com.myroom.service.IRoomService;
import com.myroom.service.IUtilityService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(UtilityInRoomAdapter utilityInRoomAdapter);
    void inject(GuestInRoomAdapter guestInRoomAdapter);
    void inject(CreateRoomActivity createRoomActivity);
    void inject(RoomAdapter roomAdapter);
    void inject(CurrencyAdapter currencyAdapter);
    void inject(CreateBillFragment createBillFragment);
    void inject(SendMessageFragment sendMessageFragment);
    void inject(GuestInRoomFragment guestInRoomFragment);
    void inject(RoomInformationAdapter roomInformationAdapter);
    void inject(IRoomService roomService);
    IRoomService getRoomService();
    ICurrencyService getCurrencyService();
    IGuestService getGuestService();
    IMessageService getMessageService();
    IUtilityService getUtilityService();
    IPaymentService getPaymentService();
}
