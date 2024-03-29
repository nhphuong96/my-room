package com.myroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myroom.R;
import com.myroom.adapter.RoomInformationAdapter;
import com.myroom.core.Constant;

public class RoomInformationFragment extends Fragment {

    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static RoomInformationFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong(Constant.ROOM_KEY_NAME, roomId);
        RoomInformationFragment fragment = new RoomInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_room_information, container, false);
        recyclerView = view.findViewById(R.id.room_information_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadRoomInformation();
    }

    private void loadRoomInformation() {
        layoutManager = new LinearLayoutManager(parentContext);
        adapter = new RoomInformationAdapter(parentContext, getArguments().getLong(Constant.ROOM_KEY_NAME));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.HORIZONTAL));
    }
}
