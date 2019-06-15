package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Utility;
import com.myroom.dto.UtilityInRoomItem;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class UtilityInRoomAdapter extends RecyclerView.Adapter<UtilityInRoomAdapter.UtilityInRoomViewHolder> {
    List<UtilityInRoomItem> utilityInRoomItemList;
    private Context context;

    @Inject
    public IRoomService roomService;

    public UtilityInRoomAdapter(Context context, Long roomId) {
        this.context = context;
        BaseApplication.getServiceComponent(context).inject(this);
        loadAllUtilitiesInRoom(roomId);
    }

    private void loadAllUtilitiesInRoom(Long roomId) {
        ReadAvailableUtilityIn readAvailableUtilityIn = new ReadAvailableUtilityIn();
        readAvailableUtilityIn.setRoomId(roomId);
        try {
            ReadAvailableUtilityOut readAvailableUtilityOut = roomService.readAvailableUtility(readAvailableUtilityIn);
            utilityInRoomItemList = readAvailableUtilityOut.getUtilityInRoomItemList();
        } catch (ValidationException e) {
            Toast.makeText(context, "ValidationException occurred while reading available utilities.", Toast.LENGTH_SHORT).show();
        } catch (OperationException e) {
            Toast.makeText(context, "OperationException occurred while reading available utilities.", Toast.LENGTH_SHORT).show();
        }
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
        UtilityInRoomItem utilityInRoomItem = utilityInRoomItemList.get(i);
        utilityInRoomViewHolder.tvUtilityInRoomName.setText(utilityInRoomItem.getUtilityName());
        utilityInRoomViewHolder.tvUtilityInRoomFee.setText(String.valueOf(utilityInRoomItem.getUtilityFee()));
    }

    @Override
    public int getItemCount() {
        return utilityInRoomItemList.size();
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
