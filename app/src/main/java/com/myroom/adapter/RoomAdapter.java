package com.myroom.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.activity.CreateBillActivity;
import com.myroom.activity.RoomDetailActivity;
import com.myroom.application.BaseApplication;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.repository.RoomRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Room;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.DeleteRoomIn;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;

    @Inject
    public RoomRepository roomRepository;
    @Inject
    public GuestRepository guestRepository;

    @Inject
    public IRoomService roomService;


    public RoomAdapter(Context context) {
        this.context = context;
        BaseApplication.getRepositoryComponent(context).inject(this);
        initializeRoomList();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView txtRoomName;
        private TextView txtGuestName;
        private AppCompatButton btnCreateBill;
        private ImageView ivAvatar;
        private long roomId;

        public RoomViewHolder(View v) {
            super(v);
            txtRoomName = v.findViewById(R.id.room_name_item);
            txtGuestName =v.findViewById(R.id.guest_name_in_room_item);
            btnCreateBill = v.findViewById(R.id.item_room_create_bill);
            ivAvatar = v.findViewById(R.id.iv_avatar);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            btnCreateBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CreateBillActivity.class);
                    intent.putExtra("roomId", roomId);
                    context.startActivity(intent);
                }
            });
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
                    .setTitle("Xóa phòng").setMessage("Bạn có chắc muốn xóa phòng này?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Room room = roomList.get(getAdapterPosition());
                            try {
                                roomService.deleteRoom(buildDeleteRoomIn(room));
                                roomList.remove(room);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóa phòng thành công", Toast.LENGTH_SHORT).show();
                            } catch (ValidationException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (OperationException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Không", null)
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
        ImageView imageView = roomViewHolder.ivAvatar;

        Room room = roomList.get(i);
        roomViewHolder.roomId = room.getId();
        txtRoomName.setText(room.getRoomName());
        Guest roomLeader = findRoomLeader(room);
        if (roomLeader != null) {
            txtLeaderName.setText(roomLeader.getGuestName());
            if (roomLeader.getGender() == 1) {
                imageView.setImageResource(context.getResources().getIdentifier(context.getString(R.string.ic_man_avatar_name), "drawable", context.getPackageName()));
            }
            else {
                imageView.setImageResource(context.getResources().getIdentifier(context.getString(R.string.ic_woman_avatar_name), "drawable", context.getPackageName()));
            }

        }
    }

    private DeleteRoomIn buildDeleteRoomIn(Room room) {
        DeleteRoomIn deleteRoomIn = new DeleteRoomIn();
        deleteRoomIn.getRoomIdList().add(room.getId());
        return deleteRoomIn;
    }

    private void initializeRoomList() {
        roomList = roomRepository.findAll();
    }

    private Guest findRoomLeader(Room room) {
        long roomId = room.getId();
        List<Guest> guestsInRoom = guestRepository.findGuestByRoomId(roomId);
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
