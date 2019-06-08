package com.myroom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RoomAttributeFragment extends Fragment {
    public static RoomAttributeFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong("roomId", roomId);
        RoomAttributeFragment fragment = new RoomAttributeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
