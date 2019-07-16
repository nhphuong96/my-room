package com.myroom.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.core.Constant;
import com.myroom.core.GenderFactory;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Room;
import com.myroom.database.repository.GuestRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IGuestService;
import com.myroom.service.sdo.DeleteGuestIn;
import com.myroom.service.sdo.DeleteGuestOut;
import com.myroom.service.sdo.ReadGuestInRoomIn;
import com.myroom.service.sdo.ReadGuestInRoomOut;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class GuestInRoomAdapter extends RecyclerView.Adapter<GuestInRoomAdapter.GuestInRoomViewHolder> {

    @Inject
    public IGuestService guestService;
    @Inject
    public GuestRepository guestRepository;
    private Context context;
    private List<Guest> guestList;
    private Long roomId;

    public GuestInRoomAdapter(Context context, Long roomId) {
        this.context = context;
        this.roomId = roomId;
        BaseApplication.getServiceComponent(context).inject(this);
        BaseApplication.getRepositoryComponent(context).inject(this);
        loadGuestInRoomList();
    }

    private void loadGuestInRoomList() {
        try {
            ReadGuestInRoomIn readGuestInRoomIn = new ReadGuestInRoomIn();
            readGuestInRoomIn.setRoomId(roomId);
            ReadGuestInRoomOut readGuestInRoomOut = guestService.readGuestInRoom(readGuestInRoomIn);
            guestList = readGuestInRoomOut.getGuestList();
        } catch (ValidationException | OperationException e) {
            Toast.makeText(context, "Không tìm tải danh sách khách phòng.", Toast.LENGTH_SHORT).show();
        }
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

    public class GuestInRoomViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
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
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa khách phòng").setMessage("Bạn có chắc muốn xóa khách này?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Guest guest = guestList.get(getAdapterPosition());
                            try {
                                DeleteGuestIn deleteGuestIn = new DeleteGuestIn();
                                deleteGuestIn.setGuestIdList(Arrays.asList(guest.getGuestKey()));
                                DeleteGuestOut deleteGuestOut = guestService.deleteGuest(deleteGuestIn);
                                if (deleteGuestOut.getDeletedIdList().contains(guest.getGuestKey())) {
                                    guestList.remove(guest);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa khách thành công", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(context, "Xóa khách không thành công", Toast.LENGTH_SHORT).show();
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

        @Override
        public void onClick(View v) {
            Guest selectedGuest = guestList.get(getAdapterPosition());
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_create_guest_in_room);

            TextView tvTitle = dialog.findViewById(R.id.dialog_create_guest_in_room_header);
            tvTitle.setText("Thay đổi thông tin khách");
            final EditText etGuestName = dialog.findViewById(R.id.create_guest_in_room_name);
            final EditText etGuestPhone = dialog.findViewById(R.id.create_guest_in_room_phone);
            final EditText etGuestIdCard = dialog.findViewById(R.id.create_guest_in_room_id_card);
            final EditText etGuestBirthday = dialog.findViewById(R.id.create_guest_in_room_birthdate);
            final RadioGroup radioGroupGender = dialog.findViewById(R.id.create_guest_in_room_gender);

            etGuestName.setText(selectedGuest.getGuestName());
            etGuestPhone.setText(selectedGuest.getPhoneNumber());
            etGuestIdCard.setText(selectedGuest.getIdCard());
            etGuestBirthday.setText(selectedGuest.getBirthDate());
            radioGroupGender.check(selectedGuest.getGender() == 1 ? R.id.radioButton_male : R.id.radioButton_female);

            AppCompatButton btnCreate = dialog.findViewById(R.id.create_guest_in_room_submit);
            AppCompatButton btnCancel = dialog.findViewById(R.id.create_guest_in_room_cancel);

            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                    final String guestName = etGuestName.getText().toString();
                    final String guestPhone = etGuestPhone.getText().toString();
                    final String guestIdCard = etGuestIdCard.getText().toString();
                    final String guestBirthday = etGuestBirthday.getText().toString();
                    if (Assert.assertEditTextNotEmpty(etGuestName, guestName) && Assert.assertEditTextNotEmpty(etGuestPhone, guestPhone)) {
                        Guest updatedGuest = guestList.get(getAdapterPosition());
                        updatedGuest.setGuestName(guestName);
                        updatedGuest.setPhoneNumber(guestPhone);
                        updatedGuest.setIdCard(guestIdCard);
                        updatedGuest.setBirthDate(guestBirthday);
                        updatedGuest.setGender(GenderFactory.getGenderAsInt(selectedGenderId));
                        boolean result = guestRepository.update(updatedGuest);
                        if (result) {
                            notifyDataSetChanged();
                            Toast.makeText(context, "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        Toast.makeText(context, "Cập nhật thông tin thất bại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            dialog.show();
        }
    }
}
