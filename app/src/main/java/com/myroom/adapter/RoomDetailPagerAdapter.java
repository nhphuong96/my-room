package com.myroom.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myroom.fragment.GuestInRoomFragment;
import com.myroom.fragment.PaymentHistoryFragment;
import com.myroom.fragment.RoomInformationFragment;
import com.myroom.fragment.UtilityInRoomFragment;

import org.apache.commons.lang3.StringUtils;

public class RoomDetailPagerAdapter extends FragmentPagerAdapter {
    private long roomId;

    public RoomDetailPagerAdapter(FragmentManager fm, long roomId) {
        super(fm);
        this.roomId = roomId;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return RoomInformationFragment.newInstance(roomId);
            case 1: return GuestInRoomFragment.newInstance(roomId);
            case 2: return UtilityInRoomFragment.newInstance(roomId);
            case 3: return PaymentHistoryFragment.newInstance(roomId);
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Thông tin";
            case 1: return "Khách phòng";
            case 2: return "Tiện ích";
            case 3: return "Hóa đơn";
            default: return StringUtils.EMPTY;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
