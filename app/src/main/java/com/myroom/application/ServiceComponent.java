package com.myroom.application;

import android.support.v7.widget.RecyclerView;

import com.myroom.activity.CreateRoomActivity;
import com.myroom.adapter.CurrencyAdapter;
import com.myroom.adapter.RoomAdapter;
import com.myroom.adapter.UtilityInRoomAdapter;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IRoomService;
import com.myroom.service.impl.RoomServiceImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(UtilityInRoomAdapter utilityInRoomAdapter);
    void inject(CreateRoomActivity createRoomActivity);
    void inject(RoomAdapter roomAdapter);
    void inject(CurrencyAdapter currencyAdapter);
    IRoomService getRoomService();
    ICurrencyService getCurrencyService();
}
