package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myroom.fragment.GuestInRoomFragment;
import com.myroom.fragment.RoomAttributeFragment;

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
            case 0: return GuestInRoomFragment.newInstance(roomId);
            case 1: return RoomAttributeFragment.newInstance(roomId);
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Guest In Room";
            case 1: return "Utilities";
            default: return StringUtils.EMPTY;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
