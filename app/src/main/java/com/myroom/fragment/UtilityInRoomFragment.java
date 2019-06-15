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
import com.myroom.adapter.UtilityInRoomAdapter;

public class UtilityInRoomFragment extends Fragment {
    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static UtilityInRoomFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong("roomId", roomId);
        UtilityInRoomFragment fragment = new UtilityInRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_utility_in_room, container, false);
        recyclerView = view.findViewById(R.id.utility_in_room_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAllUtilitiesInRoom();
    }

    private void loadAllUtilitiesInRoom() {
        long roomId = getArguments().getLong("roomId");
        if (roomId > 0) {
            layoutManager = new LinearLayoutManager(parentContext);
            adapter = new UtilityInRoomAdapter(parentContext, roomId);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.VERTICAL));
        }
        else {
            Toast.makeText(parentContext, "Could not load utilities in room", Toast.LENGTH_SHORT).show();
        }
    }
}
