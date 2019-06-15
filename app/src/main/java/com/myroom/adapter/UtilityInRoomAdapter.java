package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.service.IRoomService;
import com.myroom.service.impl.RoomServiceImpl;

public class UtilityInRoomAdapter extends RecyclerView.Adapter<UtilityInRoomAdapter.UtilityInRoomViewHolder> {

    private Context context;

    public UtilityInRoomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UtilityInRoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_utility_in_room, viewGroup, false);
        UtilityInRoomViewHolder utilityInRoomViewHolder = new UtilityInRoomViewHolder(view);
        return utilityInRoomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityInRoomViewHolder utilityInRoomViewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UtilityInRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUtilityInRoomName;
        private TextView tvUtilityInRoomFee;

        public UtilityInRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUtilityInRoomName = itemView.findViewById(R.id.item_utility_in_room_name);
            tvUtilityInRoomFee = itemView.findViewById(R.id.item_utility_in_room_fee);
        }
    }
}
