package com.myroom.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.GuestInRoomAdapter;
import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.core.Constant;
import com.myroom.core.GenderFactory;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IGuestService;
import com.myroom.service.sdo.CreateGuestIn;

import javax.inject.Inject;

public class GuestInRoomFragment extends Fragment {

    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private AppCompatButton btnCreateGuest;

    @Inject
    public IGuestService guestService;

    public static GuestInRoomFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong(Constant.ROOM_KEY_NAME, roomId);
        GuestInRoomFragment fragment = new GuestInRoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_guest_in_room, container, false);
        recyclerView = view.findViewById(R.id.guest_in_room_list);
        btnCreateGuest = view.findViewById(R.id.create_guest_in_room);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseApplication.getServiceComponent(parentContext).inject(this);
        loadGuestInRoom();
        setUpCreateGuestSubmit();
    }

    private void setUpCreateGuestSubmit() {
        btnCreateGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(parentContext);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_create_guest_in_room);

                TextView tvTitle = dialog.findViewById(R.id.dialog_create_guest_in_room_header);
                tvTitle.setText("Thêm khách mới");
                final EditText etGuestName = dialog.findViewById(R.id.create_guest_in_room_name);
                final EditText etGuestPhone = dialog.findViewById(R.id.create_guest_in_room_phone);
                final EditText etGuestIdCard = dialog.findViewById(R.id.create_guest_in_room_id_card);
                final EditText etGuestBirthday = dialog.findViewById(R.id.create_guest_in_room_birthdate);
                final RadioGroup radioGroupGender = dialog.findViewById(R.id.create_guest_in_room_gender);


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
                            try {
                                guestService.createGuest(convertReadGuestIn(guestName, guestPhone, selectedGenderId, guestBirthday, guestIdCard));
                                dialog.dismiss();
                            } catch (ValidationException | OperationException e) {
                                Toast.makeText(parentContext, "Lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
                        loadGuestInRoom();
                    }
                });
                dialog.show();
            }
        });
    }

    private CreateGuestIn convertReadGuestIn(String name, String phone, int selectedGenderId, String guestBirthday, String guestIdCard) {
        CreateGuestIn createGuestIn = new CreateGuestIn();
        createGuestIn.setGuestName(name);
        createGuestIn.setGuestPhone(phone);
        createGuestIn.setGuestBirthday(guestBirthday);
        createGuestIn.setGuestIdCard(guestIdCard);
        createGuestIn.setGender(GenderFactory.getGenderAsInt(selectedGenderId));
        createGuestIn.setRoomKey(getArguments().getLong(Constant.ROOM_KEY_NAME));
        return createGuestIn;
    }

    private void loadGuestInRoom() {
        long roomId = getArguments().getLong(Constant.ROOM_KEY_NAME);
        if (roomId != -1) {
            adapter = new GuestInRoomAdapter(parentContext, roomId);
            layoutManager = new LinearLayoutManager(parentContext);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.VERTICAL));
        }
        else {
            Toast.makeText(parentContext, "Không tìm thấy roomId từ fragment.", Toast.LENGTH_SHORT).show();
        }
    }
}
