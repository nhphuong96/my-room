package com.myroom.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.CreateRoomUtilityAdapter;
import com.myroom.application.BaseApplication;
import com.myroom.core.GenderFactory;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.CreateRoomIn;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

public class CreateRoomActivity extends AppCompatActivity {
    private EditText etRoomName;
    private EditText etGuestName;
    private EditText etBirthDate;
    private EditText etIdCard;
    private EditText etPhoneNo;
    private AppCompatButton btnSubmit;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RadioGroup radioGroupGender;
    private CreateRoomUtilityAdapter adapter;

    @Inject
    public IRoomService roomService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        BaseApplication.getServiceComponent(this).inject(this);
        initializeToolbar();
        loadAvailableUtilities();

        etRoomName = findViewById(R.id.create_room_name);
        etGuestName = findViewById(R.id.create_guest_name);
        etBirthDate = findViewById(R.id.create_guest_birthdate);
        etIdCard = findViewById(R.id.create_guest_id_card);
        etPhoneNo = findViewById(R.id.create_guest_phone_number);
        radioGroupGender = findViewById(R.id.radioButton_gender);
        btnSubmit = findViewById(R.id.create_room_submit);
        btnSubmit.setOnClickListener(btnSubmitClickedListener());
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tạo phòng");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadAvailableUtilities() {
        adapter = new CreateRoomUtilityAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.create_room_utility_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public View.OnClickListener btnSubmitClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = etRoomName.getText().toString();
                String guestName = etGuestName.getText().toString();
                String birthDate = etBirthDate.getText().toString();
                String phoneNumber = etPhoneNo.getText().toString();
                String idCard = etIdCard.getText().toString();
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                if (assertNotEmpty(etRoomName, roomName) && assertNotEmpty(etGuestName, guestName) && assertNotEmpty(etPhoneNo, phoneNumber)) {
                    try {
                        CreateRoomIn createRoomIn = buildCreateRoomIn(roomName, guestName, birthDate, idCard, phoneNumber, selectedGenderId);
                        roomService.createRoom(createRoomIn);
                        Toast.makeText(CreateRoomActivity.this, "Tạo phòng thành công.", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (ValidationException e) {
                        Toast.makeText(CreateRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (OperationException e) {
                        Toast.makeText(CreateRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    private CreateRoomIn buildCreateRoomIn(String roomName, String guestName, String birthDate, String idCard, String phoneNumber, int selectedGenderId) throws ValidationException {
        CreateRoomIn createRoomIn = new CreateRoomIn();
        createRoomIn.setRoomName(roomName);
        createRoomIn.setGuestName(guestName);
        createRoomIn.setGuestBirthDate(birthDate);
        createRoomIn.setGuestIdCard(idCard);
        createRoomIn.setGuestPhoneNumber(phoneNumber);
        createRoomIn.setGender(GenderFactory.getGenderAsInt(selectedGenderId));
        createRoomIn.setUtilityKeyList(new ArrayList<Long>());
        for (CreateRoomUtilityAdapter.UtilitySelection utilitySelection : adapter.getUtilitySelectionList()) {
            if (utilitySelection.getSelected()) {
                createRoomIn.getUtilityKeyList().add(utilitySelection.getUtility().getUtilityKey());
            }
        }

        if (CollectionUtils.isEmpty(createRoomIn.getUtilityKeyList())) {
            throw new ValidationException("Bạn phải chọn ít nhất 1 tiện ích.");
        }
        return createRoomIn;
    }

    private boolean assertNotEmpty(EditText etField, String fieldValue) {
        if (StringUtils.isEmpty(fieldValue)) {
            etField.setError("Bắt buộc");
            return false;
        }
        return true;
    }
}
