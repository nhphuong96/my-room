package com.myroom.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.activity.RoomDetailActivity;
import com.myroom.database.dao.GuestDAO;
import com.myroom.database.dao.RoomDAO;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.model.Guest;
import com.myroom.model.Room;
import com.myroom.service.IRoomService;
import com.myroom.service.impl.RoomServiceImpl;
import com.myroom.service.sdo.DeleteRoomIn;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private Context context;
    private List<Room> roomList;

    private GuestDAO guestDAO;
    private IRoomService roomService;


    public RoomAdapter(Context context) {
        this.context = context;
        guestDAO = new GuestDAO(context);
        roomService = new RoomServiceImpl(context);
        initializeRoomList(context);
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView txtRoomName;
        private TextView txtGuestName;
        private long roomId;

        public RoomViewHolder(View v) {
            super(v);
            txtRoomName = v.findViewById(R.id.room_name_item);
            txtGuestName =v.findViewById(R.id.guest_name_in_room_item);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RoomDetailActivity.class);
            intent.putExtra("roomId", roomId);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete").setMessage("Are you sure you want to delete this room?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Room room = roomList.get(getAdapterPosition());
                            try {
                                roomService.deleteRoom(buildDeleteRoomIn(room));
                                roomList.remove(room);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted room " + room.getRoomName(), Toast.LENGTH_SHORT).show();
                            } catch (ValidationException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (OperationException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
            return true;
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

    private DeleteRoomIn buildDeleteRoomIn(Room room) {
        DeleteRoomIn deleteRoomIn = new DeleteRoomIn();
        deleteRoomIn.getRoomIdList().add(room.getId());
        return deleteRoomIn;
    }

    private void initializeRoomList(Context context) {
        RoomDAO roomDAO = new RoomDAO(context);
        roomList = roomDAO.findAllRooms();
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
