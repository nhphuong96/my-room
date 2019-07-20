package com.myroom.application;

import android.content.Context;

import com.myroom.service.ICurrencyService;
import com.myroom.service.IGuestService;
import com.myroom.service.IMessageService;
import com.myroom.service.IPaymentService;
import com.myroom.service.IRoomService;
import com.myroom.service.IUtilityService;
import com.myroom.service.impl.CurrencyServiceImpl;
import com.myroom.service.impl.GuestServiceImpl;
import com.myroom.service.impl.MessageServiceImpl;
import com.myroom.service.impl.PaymentServiceImpl;
import com.myroom.service.impl.RoomServiceImpl;
import com.myroom.service.impl.UtilityServiceImpl;

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

    @Binds
    @Singleton
    abstract IMessageService bindMessageService(MessageServiceImpl messageServiceImpl);

    @Binds
    @Singleton
    abstract IUtilityService bindUtilityService(UtilityServiceImpl utilityServiceImpl);

    @Binds
    @Singleton
    abstract IPaymentService bindPaymentService(PaymentServiceImpl paymentServiceImpl);
}
