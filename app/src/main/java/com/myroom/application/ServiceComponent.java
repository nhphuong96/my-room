package com.myroom.application;

import com.myroom.activity.CreateRoomActivity;
import com.myroom.adapter.CurrencyAdapter;
import com.myroom.adapter.RoomAdapter;
import com.myroom.adapter.UtilityInRoomAdapter;
import com.myroom.fragment.CreateBillFragment;
import com.myroom.fragment.SendMessageFragment;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IRoomService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(UtilityInRoomAdapter utilityInRoomAdapter);
    void inject(CreateRoomActivity createRoomActivity);
    void inject(RoomAdapter roomAdapter);
    void inject(CurrencyAdapter currencyAdapter);
    void inject(CreateBillFragment createBillFragment);
    void inject(SendMessageFragment sendMessageFragment);
    IRoomService getRoomService();
    ICurrencyService getCurrencyService();
}
