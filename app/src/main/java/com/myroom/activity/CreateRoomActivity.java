package com.myroom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.database.dao.GuestDAO;
import com.myroom.database.dao.RoomDAO;
import com.myroom.model.Guest;
import com.myroom.model.Room;

public class CreateRoomActivity extends AppCompatActivity {

    private EditText etRoomName;
    private EditText etGuestName;
    private EditText etBirthDate;
    private EditText etIdCard;
    private EditText etPhoneNo;

    private RoomDAO roomDAO = new RoomDAO(this);
    private GuestDAO guestDAO = new GuestDAO(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etRoomName = (EditText)findViewById(R.id.create_room_name);
        etGuestName = (EditText)findViewById(R.id.create_guest_name);
        etBirthDate = (EditText)findViewById(R.id.create_guest_birthdate);
        etIdCard = (EditText)findViewById(R.id.create_guest_id_card);
        etPhoneNo = (EditText)findViewById(R.id.create_guest_phone_number);
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

    public void submitNewRoom(View view) {
        String roomName = etRoomName.getText().toString();
        String guestName = etGuestName.getText().toString();
        String birthDate = etBirthDate.getText().toString();
        String phoneNumber = etPhoneNo.getText().toString();
        String idCard = etIdCard.getText().toString();

        Room newRoom = new Room();
        newRoom.setRoomName(roomName);
        long roomId = roomDAO.addRoom(newRoom);
        if (roomId != -1) {
            Guest guestInRoom = new Guest();
            guestInRoom.setGuestName(guestName);
            guestInRoom.setBirthDate(birthDate);
            guestInRoom.setIdCard(idCard);
            guestInRoom.setPhoneNumber(phoneNumber);
            guestInRoom.setRoomId(roomId);
            long guestId = guestDAO.addGuest(guestInRoom);
            if (guestId > 0) {
                Toast.makeText(CreateRoomActivity.this, "Create room successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
