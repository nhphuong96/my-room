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
import com.myroom.core.NumberFormatter;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.RoomUtility;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.ReadUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomOut;
import com.myroom.service.sdo.UpdateUtilityInRoomIn;
import com.myroom.service.sdo.UtilityInRoomItem;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class UtilityInRoomAdapter extends RecyclerView.Adapter<UtilityInRoomAdapter.UtilityInRoomViewHolder> {
    private List<UtilityInRoomItem> utilityInRoomItemList;
    private Context context;
    private Long roomKey;
    private Currency selectedCurrency;

    @Inject
    public ICurrencyService currencyService;
    @Inject
    public IUtilityService utilityService;

    public UtilityInRoomAdapter(Context context, Long roomKey) {
        this.context = context;
        this.roomKey = roomKey;
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
        readAvailableUtilityIn.setRoomKey(roomKey);
        try {
            ReadUtilityInRoomOut readUtilityInRoomOut = utilityService.readUtilityInRoom(convertReadUtilityInRoomIn());
            utilityInRoomItemList = readUtilityInRoomOut.getUtilityInRoomItemList();
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
        utilityInRoomViewHolder.utilityKey = utilityInRoomItem.getUtilityKey();
        utilityInRoomViewHolder.tvUtilityInRoomName.setText(utilityInRoomItem.getUtilityName());
        utilityInRoomViewHolder.tvUtilityInRoomFee.setText(NumberFormatter.formatThousandNumberSeparator(String.valueOf(utilityInRoomItem.getUtilityFee())));
        utilityInRoomViewHolder.ivAvatar.setImageResource(context.getResources().getIdentifier(utilityInRoomItem.getUtilityIconName(), "drawable", context.getPackageName()));
        utilityInRoomViewHolder.tvCurrency.setText(selectedCurrency.getCurrencyId());
    }


    private ReadUtilityInRoomIn convertReadUtilityInRoomIn() {
        ReadUtilityInRoomIn readUtilityInRoomIn = new ReadUtilityInRoomIn();
        readUtilityInRoomIn.setRoomKey(roomKey);
        return readUtilityInRoomIn;
    }

    @Override
    public int getItemCount() {
        return utilityInRoomItemList.size();
    }

    public class UtilityInRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUtilityInRoomName;
        private TextView tvUtilityInRoomFee;
        private Long utilityKey;
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

                    try {
                        utilityService.updateUtilityInRoom(convertUpdateUtilityInRoomIn(etUtilityFee.getText().toString()));
                        UtilityInRoomItem utilityInRoomItem = utilityInRoomItemList.get(getAdapterPosition());
                        utilityInRoomItem.setUtilityFee(etUtilityFee.getText().toString());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                    catch (ValidationException | OperationException e) {
                        Toast.makeText(context, "Không thể cập nhật phí, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }

        private UpdateUtilityInRoomIn convertUpdateUtilityInRoomIn(String utilityFee) {
            UpdateUtilityInRoomIn updateUtilityInRoomIn = new UpdateUtilityInRoomIn();
            updateUtilityInRoomIn.setRoomKey(roomKey);
            updateUtilityInRoomIn.setUtilityKey(utilityKey);
            updateUtilityInRoomIn.setUtilityFee(utilityFee);
            return updateUtilityInRoomIn;
        }
    }
}
