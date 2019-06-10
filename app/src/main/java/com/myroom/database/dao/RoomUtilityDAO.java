package com.myroom.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.database.DatabaseHelper;
import com.myroom.model.BaseModel;
import com.myroom.model.Room;
import com.myroom.model.RoomUtility;
import com.myroom.model.Utility;

public class RoomUtilityDAO {
    private DatabaseHelper dbHelper;

    public RoomUtilityDAO(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    public long addRoomUtility(long roomId, long utilityId, double utilityFee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomUtility.Column.COLUMN_ROOM_ID.getColName(), roomId);
        values.put(RoomUtility.Column.COLUMN_UTILITY_ID.getColName(), utilityId);
        values.put(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(), utilityFee);
        long id = db.insertOrThrow(RoomUtility.TABLE_NAME, null, values);
        return id;
    }

    public RoomUtility findRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_ID.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_ID.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                RoomUtility.Column.COLUMN_ROOM_ID.getColName() + " = ?"
                        + RoomUtility.Column.COLUMN_UTILITY_ID.getColName() + " = ?"
                ,
                new String[] {String.valueOf(roomId), String.valueOf(utilityId)}, null, null, null, null);

        RoomUtility result = new RoomUtility();
        result.setRoomId(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_ROOM_ID.getIndex())));
        result.setUtilityId(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_UTILITY_ID.getIndex())));
        result.setUtilityFee(Double.valueOf(c.getString(RoomUtility.Column.COLUMN_UTILITY_FEE.getIndex())));
        return result;
    }


    public boolean deleteRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Utility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_ID.getColName() + " = ?"
                + RoomUtility.Column.COLUMN_UTILITY_ID.getColName() + " = ?",
                new String[]{String.valueOf(roomId), String.valueOf(utilityId)});
        return rowAffected > 0;
    }

    public boolean updateUtility(RoomUtility newRoomUtility) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(), newRoomUtility.getUtilityFee());
        int rowAffected = db.update(Utility.TABLE_NAME,
                values,
                RoomUtility.Column.COLUMN_ROOM_ID.getColName() + " = ?"
                + RoomUtility.Column.COLUMN_UTILITY_ID.getColName() + " = ?",
                new String[]{String.valueOf(newRoomUtility.getRoomId()), String.valueOf(newRoomUtility.getUtilityId())});
        return rowAffected > 0;
    }
}
