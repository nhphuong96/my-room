package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.Guest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GuestRepository implements IObjectRepository<Guest> {
    private DatabaseHelper dbHelper;

    @Inject
    public GuestRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
    }

    public List<Guest> findGuestByRoomId(long roomId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                Guest.Column.COLUMN_GUEST_KEY.getColName(),
                Guest.Column.COLUMN_GUEST_NAME.getColName(),
                Guest.Column.COLUMN_BIRTH_DATE.getColName(),
                Guest.Column.COLUMN_ID_CARD.getColName(),
                Guest.Column.COLUMN_PHONE_NUMBER.getColName(),
                Guest.Column.COLUMN_ROOM_ID.getColName(),
                Guest.Column.COLUMN_GENDER.getColName()
        };
        Cursor c = db.query(Guest.TABLE_NAME, columns, Guest.Column.COLUMN_ROOM_ID.getColName() + " = ?", new String[] {String.valueOf(roomId)}, null, null, null, null);

        List<Guest> guestsInRoom = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Guest guest = new Guest();
                guest.setGuestKey(Integer.parseInt(c.getString(Guest.Column.COLUMN_GUEST_KEY.getIndex())));
                guest.setGuestName(c.getString(Guest.Column.COLUMN_GUEST_NAME.getIndex()));
                guest.setBirthDate(c.getString(Guest.Column.COLUMN_BIRTH_DATE.getIndex()));
                guest.setIdCard(c.getString(Guest.Column.COLUMN_ID_CARD.getIndex()));
                guest.setPhoneNumber(c.getString(Guest.Column.COLUMN_PHONE_NUMBER.getIndex()));
                guest.setRoomId(Long.valueOf(c.getString(Guest.Column.COLUMN_ROOM_ID.getIndex())));
                guest.setGender(Integer.valueOf(c.getString(Guest.Column.COLUMN_GENDER.getIndex())));
                guestsInRoom.add(guest);
            }
            while (c.moveToNext());
        }
        return guestsInRoom;
    }

    @Override
    public long add(Guest entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Guest.Column.COLUMN_GUEST_NAME.getColName(), entity.getGuestName());
        values.put(Guest.Column.COLUMN_BIRTH_DATE.getColName(), entity.getBirthDate());
        values.put(Guest.Column.COLUMN_ID_CARD.getColName(), entity.getIdCard());
        values.put(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), entity.getPhoneNumber());
        values.put(Guest.Column.COLUMN_ROOM_ID.getColName(), entity.getRoomId());
        values.put(Guest.Column.COLUMN_GENDER.getColName(), entity.getGender());
        long id = db.insertOrThrow(Guest.TABLE_NAME, null, values);
        return id;
    }

    @Override
    public Guest find(long key) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                Guest.Column.COLUMN_GUEST_KEY.getColName(),
                Guest.Column.COLUMN_GUEST_NAME.getColName(),
                Guest.Column.COLUMN_BIRTH_DATE.getColName(),
                Guest.Column.COLUMN_ID_CARD.getColName(),
                Guest.Column.COLUMN_PHONE_NUMBER.getColName(),
                Guest.Column.COLUMN_ROOM_ID.getColName(),
                Guest.Column.COLUMN_GENDER.getColName()
        };
        Cursor c = db.query(Guest.TABLE_NAME, columns, Guest.Column.COLUMN_GUEST_KEY.getColName() + " = ?", new String[] {String.valueOf(key)}, null, null, null, null);

        if (c.moveToFirst()) {
            Guest result = new Guest();
            result.setGuestKey(Integer.parseInt(c.getString(Guest.Column.COLUMN_GUEST_KEY.getIndex())));
            result.setGuestName(c.getString(Guest.Column.COLUMN_GUEST_NAME.getIndex()));
            result.setBirthDate(c.getString(Guest.Column.COLUMN_BIRTH_DATE.getIndex()));
            result.setIdCard(c.getString(Guest.Column.COLUMN_ID_CARD.getIndex()));
            result.setPhoneNumber(c.getString(Guest.Column.COLUMN_PHONE_NUMBER.getIndex()));
            result.setRoomId(Long.valueOf(c.getString(Guest.Column.COLUMN_ROOM_ID.getIndex())));
            result.setGender(Integer.valueOf(c.getString(Guest.Column.COLUMN_GENDER.getIndex())));
            return result;
        }
        return null;
    }

    @Override
    public List<Guest> findAll() {
        return null;
    }

    @Override
    public boolean delete(long key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Guest.TABLE_NAME, Guest.Column.COLUMN_GUEST_KEY.getColName() + " = ?", new String[]{String.valueOf(key)});
        return rowAffected > 0;
    }

    @Override
    public boolean update(Guest entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Guest.Column.COLUMN_GUEST_NAME.getColName(), entity.getGuestName());
        values.put(Guest.Column.COLUMN_BIRTH_DATE.getColName(), entity.getBirthDate());
        values.put(Guest.Column.COLUMN_ID_CARD.getColName(), entity.getIdCard());
        values.put(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), entity.getPhoneNumber());
        values.put(Guest.Column.COLUMN_ROOM_ID.getColName(), entity.getRoomId());
        values.put(Guest.Column.COLUMN_GENDER.getColName(), entity.getGender());
        int rowAffected = db.update(Guest.TABLE_NAME, values, Guest.Column.COLUMN_GUEST_KEY.getColName() + " = ?", new String[]{String.valueOf(entity.getGuestKey())});
        return rowAffected > 0;
    }
}
