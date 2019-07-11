package com.myroom.application;

import android.content.Context;

import com.myroom.service.ICurrencyService;
import com.myroom.service.IGuestService;
import com.myroom.service.IRoomService;
import com.myroom.service.impl.CurrencyServiceImpl;
import com.myroom.service.impl.GuestServiceImpl;
import com.myroom.service.impl.RoomServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds
    @Singleton
    abstract IRoomService bindRoomService(RoomServiceImpl roomServiceImpl);

    @Binds
    @Singleton
    abstract ICurrencyService bindCurrencyService(CurrencyServiceImpl currencyServiceImpl);

    @Binds
    @Singleton
    abstract IGuestService bindGuestService(GuestServiceImpl guestServiceImpl);
}
