package com.myroom.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.database.DatabaseHelper;
import com.myroom.model.BaseModel;
import com.myroom.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private DatabaseHelper dbHelper;

    public RoomDAO(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    public long addRoom(Room room) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Room.Column.COLUMN_ROOM_NAME.getColName(), room.getRoomName());
        long id = db.insertOrThrow(Room.TABLE_NAME, null, values);
        return id;
    }

    public Room findRoom(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Room.Column.COLUMN_ROOM_NAME.getColName(),
        };
        Cursor c = db.query(Room.TABLE_NAME, columns, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        Room result = new Room();
        result.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
        result.setRoomName(c.getString(Room.Column.COLUMN_ROOM_NAME.getIndex()));
        return result;
    }

    public boolean deleteRoom(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Room.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(id)});
        return rowAffected > 0;
    }

    public boolean updateRoom(Room newRoom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Room.Column.COLUMN_ROOM_NAME.getColName(), newRoom.getRoomName());
        int rowAffected = db.update(Room.TABLE_NAME, values, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(newRoom.getId())});
        return rowAffected > 0;
    }

    public List<Room> findAllRooms() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Room.TABLE_NAME, null);

        List<Room> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Room room = new Room();
                room.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
                room.setRoomName(c.getString(Room.Column.COLUMN_ROOM_NAME.getIndex()));
                result.add(room);
            }
            while (c.moveToNext());
        }
        return result;
    }
}
