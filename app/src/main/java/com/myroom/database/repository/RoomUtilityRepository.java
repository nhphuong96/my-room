package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.RoomUtility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoomUtilityRepository {
    private DatabaseHelper dbHelper;

    @Inject
    public RoomUtilityRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
    }

    public long addRoomUtility(long roomKey, long utilityKey, double utilityFee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomUtility.Column.COLUMN_ROOM_KEY.getColName(), roomKey);
        values.put(RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(), utilityKey);
        values.put(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(), utilityFee);
        long id = db.insertOrThrow(RoomUtility.TABLE_NAME, null, values);
        return id;
    }

    public List<RoomUtility> findRoomUtility(long roomKey, long utilityKey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ? AND "
                        + RoomUtility.Column.COLUMN_UTILITY_KEY.getColName() + " = ?",
                    new String[] { String.valueOf(roomKey), String.valueOf(utilityKey) }, null, null, null, null);

        List<RoomUtility> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                RoomUtility roomUtility = new RoomUtility();
                roomUtility.setRoomKey(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_ROOM_KEY.getIndex())));
                roomUtility.setUtilityKey(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_UTILITY_KEY.getIndex())));
                roomUtility.setUtilityFee(c.getString(RoomUtility.Column.COLUMN_UTILITY_FEE.getIndex()));
                result.add(roomUtility);
            }
            while (c.moveToNext());
        }

        return result;
    }

    public List<RoomUtility> findRoomUtilityByRoomKey(long roomKey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                    RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?",
                    new String[] {String.valueOf(roomKey)}, null, null, null, null);

        List<RoomUtility> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                RoomUtility roomUtility = new RoomUtility();
                roomUtility.setRoomKey(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_ROOM_KEY.getIndex())));
                roomUtility.setUtilityKey(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_UTILITY_KEY.getIndex())));
                roomUtility.setUtilityFee(c.getString(RoomUtility.Column.COLUMN_UTILITY_FEE.getIndex()));
                result.add(roomUtility);
            }
            while (c.moveToNext());
        }

        return result;
    }

    public boolean deleteRoomUtility(long roomKey, long utilityKey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?"
                + RoomUtility.Column.COLUMN_UTILITY_KEY.getColName() + " = ?",
                new String[]{String.valueOf(roomKey), String.valueOf(utilityKey)});
        return rowAffected > 0;
    }

    public boolean deleteRoomUtilityByRoomKey(long roomKey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?",
                new String[]{String.valueOf(roomKey)});
        return rowAffected > 0;
    }

    public boolean updateRoomUtility(RoomUtility newRoomUtility) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(), newRoomUtility.getUtilityFee());
        int rowAffected = db.update(RoomUtility.TABLE_NAME,
                values,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?"
                + " AND " + RoomUtility.Column.COLUMN_UTILITY_KEY.getColName() + " = ?",
                new String[]{String.valueOf(newRoomUtility.getRoomKey()), String.valueOf(newRoomUtility.getUtilityKey())});
        return rowAffected > 0;
    }
}
