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

    public long addRoomUtility(long roomId, long utilityId, int utilityFee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomUtility.Column.COLUMN_ROOM_KEY.getColName(), roomId);
        values.put(RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(), utilityId);
        values.put(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(), utilityFee);
        long id = db.insertOrThrow(RoomUtility.TABLE_NAME, null, values);
        return id;
    }

    public List<RoomUtility> findRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ? AND "
                        + RoomUtility.Column.COLUMN_UTILITY_KEY.getColName() + " = ?",
                    new String[] { String.valueOf(roomId), String.valueOf(utilityId) }, null, null, null, null);

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

    public List<RoomUtility> findRoomUtilityByRoomId(long roomId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                    RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?",
                    new String[] {String.valueOf(roomId)}, null, null, null, null);

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

    public boolean deleteRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?"
                + RoomUtility.Column.COLUMN_UTILITY_KEY.getColName() + " = ?",
                new String[]{String.valueOf(roomId), String.valueOf(utilityId)});
        return rowAffected > 0;
    }

    public boolean deleteRoomUtilityByRoomId(long roomId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_KEY.getColName() + " = ?",
                new String[]{String.valueOf(roomId)});
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
