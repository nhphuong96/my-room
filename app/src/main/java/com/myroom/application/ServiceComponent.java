package com.myroom.application;

import com.myroom.activity.CreateRoomActivity;
import com.myroom.adapter.RoomAdapter;
import com.myroom.service.IRoomService;
import com.myroom.service.impl.RoomServiceImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(CreateRoomActivity createRoomActivity);
    void inject(RoomAdapter roomAdapter);
    IRoomService getRoomService();
}
