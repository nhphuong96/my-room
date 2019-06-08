package com.myroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.activity.RoomDetailActivity;
import com.myroom.database.dao.GuestDAO;
import com.myroom.model.Guest;
import com.myroom.model.Room;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private Context context;
    private List<Room> roomList;
    private GuestDAO guestDAO;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.roomList = roomList;
        this.context = context;
        guestDAO = new GuestDAO(context);
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtRoomName;
        private TextView txtGuestName;
        private long roomId;

        public RoomViewHolder(View v) {
            super(v);
            txtRoomName = v.findViewById(R.id.room_name_item);
            txtGuestName =v.findViewById(R.id.guest_name_in_room_item);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RoomDetailActivity.class);
            intent.putExtra("roomId", roomId);
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room, parent, false);
        RoomViewHolder roomViewHolder = new RoomViewHolder(view);
        return roomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder roomViewHolder, int i) {
        TextView txtRoomName = roomViewHolder.txtRoomName;
        TextView txtLeaderName = roomViewHolder.txtGuestName;

        Room room = roomList.get(i);
        roomViewHolder.roomId = room.getId();
        txtRoomName.setText(room.getRoomName());
        Guest roomLeader = findRoomLeader(room);
        if (roomLeader != null) {
            txtLeaderName.setText(roomLeader.getGuestName());
        }
    }

    private Guest findRoomLeader(Room room) {
        long roomId = room.getId();
        List<Guest> guestsInRoom = guestDAO.findGuestByRoomId(roomId);
        if (CollectionUtils.isNotEmpty(guestsInRoom)) {
            return guestsInRoom.get(0);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
