package com.myroom.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.dto.UtilityInRoomItem;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class UtilityInRoomAdapter extends RecyclerView.Adapter<UtilityInRoomAdapter.UtilityInRoomViewHolder> {
    private List<UtilityInRoomItem> utilityInRoomItemList;
    private Context context;
    private Long roomId;
    private Currency selectedCurrency;

    @Inject
    public IRoomService roomService;
    @Inject
    public RoomUtilityRepository roomUtilityRepository;
    @Inject
    public ICurrencyService currencyService;

    public UtilityInRoomAdapter(Context context, Long roomId) {
        this.context = context;
        this.roomId = roomId;
        BaseApplication.getServiceComponent(context).inject(this);
        BaseApplication.getRepositoryComponent(context).inject(this);
        loadAllUtilitiesInRoom();
        loadSelectedCurrency();
    }

    private void loadSelectedCurrency() {
        try {
            ReadSelectedCurrencyOut readSelectedCurrencyOut = currencyService.readSelectedCurrency();
            selectedCurrency = readSelectedCurrencyOut.getCurrency();
        } catch (OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra khi đọc loại tiền tệ.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAllUtilitiesInRoom() {
        ReadAvailableUtilityIn readAvailableUtilityIn = new ReadAvailableUtilityIn();
        readAvailableUtilityIn.setRoomId(roomId);
        try {
            ReadAvailableUtilityOut readAvailableUtilityOut = roomService.readAvailableUtility(readAvailableUtilityIn);
            utilityInRoomItemList = readAvailableUtilityOut.getUtilityInRoomItemList();
        } catch (ValidationException e) {
            Toast.makeText(context, "ValidationException: Có lỗi xảy ra, không thể tải tiện ích phòng.", Toast.LENGTH_SHORT).show();
        } catch (OperationException e) {
            Toast.makeText(context, "OperationException: Có lỗi xảy ra, không thể tải tiện ích phòng.", Toast.LENGTH_SHORT).show();
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
        utilityInRoomViewHolder.utilityId = utilityInRoomItem.getUtilityId();
        utilityInRoomViewHolder.tvUtilityInRoomName.setText(utilityInRoomItem.getUtilityName());
        utilityInRoomViewHolder.tvUtilityInRoomFee.setText(String.valueOf(utilityInRoomItem.getUtilityFee()));
        utilityInRoomViewHolder.ivAvatar.setImageResource(context.getResources().getIdentifier(utilityInRoomItem.getUtilityIconName(), "drawable", context.getPackageName()));
        utilityInRoomViewHolder.tvCurrency.setText(selectedCurrency.getCurrencyCd());
    }

    @Override
    public int getItemCount() {
        return utilityInRoomItemList.size();
    }

    public class UtilityInRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUtilityInRoomName;
        private TextView tvUtilityInRoomFee;
        private Long utilityId;
        private ImageView ivAvatar;
        private TextView tvCurrency;

        public UtilityInRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUtilityInRoomName = itemView.findViewById(R.id.item_utility_in_room_name);
            tvUtilityInRoomFee = itemView.findViewById(R.id.item_utility_in_room_fee);
            ivAvatar = itemView.findViewById(R.id.item_utility_in_room_icon);
            tvCurrency = itemView.findViewById(R.id.item_utility_currency);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            editUtilityFee();
        }

        private void editUtilityFee() {
            UtilityInRoomItem currentUtility = utilityInRoomItemList.get(getAdapterPosition());
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_utility);

            final TextView tvTitle = dialog.findViewById(R.id.dialog_edit_utility_header);
            tvTitle.setText(currentUtility.getUtilityName());

            final EditText etUtilityFee = dialog.findViewById(R.id.edit_utility_fee);
            etUtilityFee.setText(String.valueOf(currentUtility.getUtilityFee()));

            AppCompatButton btnCancel = dialog.findViewById(R.id.edit_utility_cancel);
            AppCompatButton btnSave = dialog.findViewById(R.id.edit_utility_submit);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(etUtilityFee.getText())) {
                        etUtilityFee.setError("Bắt buộc");
                        return;
                    }

                    RoomUtility roomUtility = new RoomUtility();
                    roomUtility.setRoomId(roomId);
                    roomUtility.setUtilityId(utilityId);
                    roomUtility.setUtilityFee(etUtilityFee.getText().toString());
                    boolean success = roomUtilityRepository.updateRoomUtility(roomUtility);
                    if (success) {
                        UtilityInRoomItem utilityInRoomItem = utilityInRoomItemList.get(getAdapterPosition());
                        utilityInRoomItem.setUtilityFee(etUtilityFee.getText().toString());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Không thể cập nhật phí, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }
    }

}
