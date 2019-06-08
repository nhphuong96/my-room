package com.myroom.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.database.DatabaseHelper;
import com.myroom.model.BaseModel;
import com.myroom.model.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    private DatabaseHelper dbHelper;

    public GuestDAO(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    public long addGuest(Guest guest) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Guest.Column.COLUMN_GUEST_NAME.getColName(), guest.getGuestName());
        values.put(Guest.Column.COLUMN_BIRTH_DATE.getColName(), guest.getBirthDate());
        values.put(Guest.Column.COLUMN_ID_CARD.getColName(), guest.getIdCard());
        values.put(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), guest.getPhoneNumber());
        values.put(Guest.Column.COLUMN_ROOM_ID.getColName(), guest.getRoomId());
        long id = db.insertOrThrow(Guest.TABLE_NAME, null, values);
        return id;
    }

    public Guest findGuest(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Guest.Column.COLUMN_GUEST_NAME.getColName(),
                Guest.Column.COLUMN_BIRTH_DATE.getColName(),
                Guest.Column.COLUMN_ID_CARD.getColName(),
                Guest.Column.COLUMN_PHONE_NUMBER.getColName()
        };
        Cursor c = db.query(Guest.TABLE_NAME, columns, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        Guest result = new Guest();
        result.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
        result.setGuestName(c.getString(Guest.Column.COLUMN_GUEST_NAME.getIndex()));
        result.setBirthDate(c.getString(Guest.Column.COLUMN_BIRTH_DATE.getIndex()));
        result.setIdCard(c.getString(Guest.Column.COLUMN_ID_CARD.getIndex()));
        result.setPhoneNumber(c.getString(Guest.Column.COLUMN_PHONE_NUMBER.getIndex()));
        return result;
    }

    public List<Guest> findGuestByRoomId(long roomId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Guest.Column.COLUMN_GUEST_NAME.getColName(),
                Guest.Column.COLUMN_BIRTH_DATE.getColName(),
                Guest.Column.COLUMN_ID_CARD.getColName(),
                Guest.Column.COLUMN_PHONE_NUMBER.getColName()
        };
        Cursor c = db.query(Guest.TABLE_NAME, columns, Guest.Column.COLUMN_ROOM_ID.getColName() + " = ?", new String[] {String.valueOf(roomId)}, null, null, null, null);

        List<Guest> guestsInRoom = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Guest guest = new Guest();
                guest.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
                guest.setGuestName(c.getString(Guest.Column.COLUMN_GUEST_NAME.getIndex()));
                guest.setBirthDate(c.getString(Guest.Column.COLUMN_BIRTH_DATE.getIndex()));
                guest.setIdCard(c.getString(Guest.Column.COLUMN_ID_CARD.getIndex()));
                guest.setPhoneNumber(c.getString(Guest.Column.COLUMN_PHONE_NUMBER.getIndex()));
                guestsInRoom.add(guest);
            }
            while (c.moveToNext());
        }
        return guestsInRoom;
    }

    public boolean deleteGuest(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Guest.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(id)});
        return rowAffected > 0;
    }

    public boolean updateGuest(Guest newGuest) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Guest.Column.COLUMN_GUEST_NAME.getColName(), newGuest.getGuestName());
        values.put(Guest.Column.COLUMN_BIRTH_DATE.getColName(), newGuest.getBirthDate());
        values.put(Guest.Column.COLUMN_ID_CARD.getColName(), newGuest.getIdCard());
        values.put(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), newGuest.getPhoneNumber());
        int rowAffected = db.update(Guest.TABLE_NAME, values, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(newGuest.getId())});
        return rowAffected > 0;
    }
}
