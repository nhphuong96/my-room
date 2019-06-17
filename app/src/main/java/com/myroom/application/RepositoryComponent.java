package com.myroom.application;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.myroom.activity.CreateBillActivity;
import com.myroom.activity.GeneralSettingUtilityActivity;
import com.myroom.adapter.CreateRoomUtilityAdapter;
import com.myroom.adapter.GuestInRoomAdapter;
import com.myroom.adapter.RoomAdapter;
import com.myroom.adapter.UtilityAdapter;
import com.myroom.adapter.UtilityInRoomAdapter;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.repository.RoomRepository;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.service.IRoomService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        ServiceModule.class
})
public interface RepositoryComponent {
    void inject(RoomAdapter roomAdapter);
    void inject(CreateRoomUtilityAdapter createRoomUtilityAdapter);
    void inject(UtilityAdapter utilityAdapter);
    void inject(UtilityInRoomAdapter utilityInRoomAdapter);
    void inject(GuestInRoomAdapter guestInRoomAdapter);

    void inject(IRoomService roomService);

    void inject(GeneralSettingUtilityActivity generalSettingUtilityActivity);
    void inject(CreateBillActivity createBillActivity);

    RoomRepository getRoomRepository();
    GuestRepository getGuestRepository();
    UtilityRepository getUtilityRepository();
    RoomUtilityRepository getRoomUtilityRepository();
}