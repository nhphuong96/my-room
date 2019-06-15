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
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.GuestInRoomAdapter;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.dao.Guest;

import java.util.List;

public class GuestInRoomFragment extends Fragment {
    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static GuestInRoomFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong("roomId", roomId);
        GuestInRoomFragment fragment = new GuestInRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_guest_in_room, container, false);
        recyclerView = view.findViewById(R.id.guest_in_room_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadGuestInRoom();
    }

    private void loadGuestInRoom() {
        long roomId = getArguments().getLong("roomId");
        if (roomId != -1) {
            adapter = new GuestInRoomAdapter(parentContext, roomId);
            layoutManager = new LinearLayoutManager(parentContext);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.VERTICAL));
        }
        else {
            Toast.makeText(parentContext, "Could not get roomId in fragment.", Toast.LENGTH_SHORT).show();
        }

    }
}
