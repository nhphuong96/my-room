package com.myroom.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.Room;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class RoomRepository implements IObjectRepository<Room> {
    private DatabaseHelper dbHelper;

    @Inject
    public RoomRepository() {
        Context context = BaseApplication.getContextComponent().getContext();
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public long add(Room entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Room.Column.COLUMN_ROOM_NAME.getColName(), entity.getRoomName());
        long id = db.insertOrThrow(Room.TABLE_NAME, null, values);
        return id;
    }

    @Override
    public Room find(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                Room.Column.COLUMN_ROOM_KEY.getColName(),
                Room.Column.COLUMN_ROOM_NAME.getColName(),
        };
        Cursor c = db.query(Room.TABLE_NAME, columns, Room.Column.COLUMN_ROOM_KEY.getColName() + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (c.moveToFirst()) {
            Room result = new Room();
            result.setRoomKey(Integer.parseInt(c.getString(Room.Column.COLUMN_ROOM_KEY.getIndex())));
            result.setRoomName(c.getString(Room.Column.COLUMN_ROOM_NAME.getIndex()));
            return result;
        }
        return null;
    }

    @Override
    public List<Room> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Room.TABLE_NAME, null);

        List<Room> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Room room = new Room();
                room.setRoomKey(Integer.parseInt(c.getString(Room.Column.COLUMN_ROOM_KEY.getIndex())));
                room.setRoomName(c.getString(Room.Column.COLUMN_ROOM_NAME.getIndex()));
                result.add(room);
            }
            while (c.moveToNext());
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Room.TABLE_NAME, Room.Column.COLUMN_ROOM_KEY.getColName() + " = ?", new String[]{String.valueOf(id)});
        return rowAffected > 0;
    }

    @Override
    public boolean update(Room entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Room.Column.COLUMN_ROOM_NAME.getColName(), entity.getRoomName());
        int rowAffected = db.update(Room.TABLE_NAME, values, Room.Column.COLUMN_ROOM_KEY.getColName() + " = ?", new String[]{String.valueOf(entity.getRoomKey())});
        return rowAffected > 0;
    }
}
