package com.myroom.application;

import android.content.Context;

import com.myroom.service.IRoomService;
import com.myroom.service.impl.RoomServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds
    @Singleton
    abstract IRoomService bindRoomService(RoomServiceImpl roomServiceImpl);
}
