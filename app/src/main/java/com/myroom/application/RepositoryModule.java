package com.myroom.application;

import android.content.Context;

import com.myroom.database.repository.CurrencyRepository;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.repository.PaymentRepository;
import com.myroom.database.repository.RoomRepository;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.database.repository.UtilityRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    private final Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    RoomRepository provideRoomRepository() {
        return new RoomRepository();
    }

    @Provides
    @Singleton
    GuestRepository provideGuestRepository() {
        return new GuestRepository();
    }

    @Provides
    @Singleton
    UtilityRepository provideUtilityRepository() {
        return new UtilityRepository();
    }

    @Provides
    @Singleton
    RoomUtilityRepository provideRoomUtilityRepository() {
        return new RoomUtilityRepository();
    }

    @Provides
    @Singleton
    CurrencyRepository provideCurrencyRepository() {
        return new CurrencyRepository();
    }

    @Provides
    @Singleton
    PaymentRepository providePaymentRepository() {
        return new PaymentRepository();
    }
}
