package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Guest;
import com.myroom.database.repository.GuestRepository;

import java.util.List;

import javax.inject.Inject;

public class GuestInRoomAdapter extends RecyclerView.Adapter<GuestInRoomAdapter.GuestInRoomViewHolder> {

    @Inject
    public GuestRepository guestRepository;
    private Context context;
    private List<Guest> guestList;
    private Long roomId;

    public GuestInRoomAdapter(Context context, Long roomId) {
        this.context = context;
        BaseApplication.getRepositoryComponent(context).inject(this);
        this.roomId = roomId;
        loadGuestInRoomList();
    }

    private void loadGuestInRoomList() {
        guestList = guestRepository.findGuestByRoomId(roomId);
    }

    @NonNull
    @Override
    public GuestInRoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_guest_in_room, viewGroup, false);
        GuestInRoomViewHolder guestInRoomViewHolder = new GuestInRoomViewHolder(view);
        return guestInRoomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GuestInRoomViewHolder guestInRoomViewHolder, int i) {
        Guest guest = guestList.get(i);
        guestInRoomViewHolder.tvGuestName.setText(guest.getGuestName());
        guestInRoomViewHolder.tvBirthDate.setText(guest.getBirthDate());
        if (guest.getGender() == 1) {
            guestInRoomViewHolder.ivAvatar.setImageResource(context.getResources().getIdentifier(context.getString(R.string.ic_man_avatar_name), "drawable", context.getPackageName()));
        }
        else {
            guestInRoomViewHolder.ivAvatar.setImageResource(context.getResources().getIdentifier(context.getString(R.string.ic_woman_avatar_name), "drawable", context.getPackageName()));
        }
    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    public class GuestInRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGuestName;
        private TextView tvBirthDate;
        private TextView tvIdCard;
        private TextView tvPhoneNumber;
        private ImageView ivAvatar;

        public GuestInRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuestName = itemView.findViewById(R.id.item_guest_in_room_name);
            tvBirthDate = itemView.findViewById(R.id.item_guest_in_room_birth_date);
            ivAvatar = itemView.findViewById(R.id.item_guest_in_room_avatar);
        }
    }
}
