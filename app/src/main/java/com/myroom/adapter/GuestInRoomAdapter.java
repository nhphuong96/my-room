package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.model.Guest;

import org.w3c.dom.Text;

import java.util.List;

public class GuestInRoomAdapter extends RecyclerView.Adapter<GuestInRoomAdapter.GuestInRoomViewHolder> {

    private List<Guest> guestList;

    public GuestInRoomAdapter(List<Guest> guestList) {
        this.guestList = guestList;
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

        public GuestInRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuestName = itemView.findViewById(R.id.item_guest_in_room_name);
            tvBirthDate = itemView.findViewById(R.id.item_guest_in_room_birth_date);
        }
    }
}
