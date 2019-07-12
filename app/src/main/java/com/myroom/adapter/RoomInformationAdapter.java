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
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.ReadRoomInformationIn;
import com.myroom.service.sdo.ReadRoomInformationOut;
import com.myroom.service.sdo.RoomInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoomInformationAdapter extends RecyclerView.Adapter<RoomInformationAdapter.RoomInformationViewHolder> {

    private Context context;
    private long roomId;
    private List<RoomInfo> roomInfoList;
    @Inject
    public IRoomService roomService;

    public RoomInformationAdapter(Context context, long roomId) {
        this.context = context;
        this.roomId = roomId;
        roomInfoList = new ArrayList<>();
        BaseApplication.getServiceComponent(context).inject(this);
        loadRoomInformationList();
    }

    private void loadRoomInformationList() {
        try {
            ReadRoomInformationIn readRoomInformationIn = new ReadRoomInformationIn();
            readRoomInformationIn.setRoomId(roomId);
            ReadRoomInformationOut readRoomInformationOut = roomService.readRoomInformation(readRoomInformationIn);
            roomInfoList.addAll(readRoomInformationOut.getRoomInfoList());

        } catch (ValidationException | OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public RoomInformationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_room_information, viewGroup, false);
        RoomInformationViewHolder roomInformationViewHolder = new RoomInformationViewHolder(view);
        return roomInformationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomInformationViewHolder roomInformationViewHolder, int i) {
        RoomInfo roomInfo = roomInfoList.get(i);
        roomInformationViewHolder.tvKey.setText(roomInfo.getKey());
        roomInformationViewHolder.tvValue.setText(roomInfo.getValue());
    }

    @Override
    public int getItemCount() {
        return roomInfoList.size();
    }

    public class RoomInformationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKey;
        private TextView tvValue;

        public RoomInformationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.item_room_information_key);
            tvValue = itemView.findViewById(R.id.item_room_information_value);
        }
    }
}
