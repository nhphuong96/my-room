package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoomUtilityRepository {
    private DatabaseHelper dbHelper;

    @Inject
    public RoomUtilityRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
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

    public List<RoomUtility> findRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                RoomUtility.Column.COLUMN_ROOM_ID.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_ID.getColName(),
                RoomUtility.Column.COLUMN_UTILITY_FEE.getColName()
        };
        StringBuilder preparedStatement = new StringBuilder();
        preparedStatement.append(RoomUtility.Column.COLUMN_ROOM_ID.getColName());
        preparedStatement.append(" = ? ");
        if (utilityId != 0) {
            preparedStatement.append(" AND ");
            preparedStatement.append(RoomUtility.Column.COLUMN_UTILITY_ID.getColName());
            preparedStatement.append(" = ? ");
        }
        Cursor c = db.query(RoomUtility.TABLE_NAME, columns,
                preparedStatement.toString(),
                new String[] {String.valueOf(roomId), String.valueOf(utilityId)}, null, null, null, null);

        List<RoomUtility> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                RoomUtility roomUtility = new RoomUtility();
                roomUtility.setRoomId(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_ROOM_ID.getIndex())));
                roomUtility.setUtilityId(Integer.parseInt(c.getString(RoomUtility.Column.COLUMN_UTILITY_ID.getIndex())));
                roomUtility.setUtilityFee(Double.valueOf(c.getString(RoomUtility.Column.COLUMN_UTILITY_FEE.getIndex())));
                result.add(roomUtility);
            }
            while (c.moveToNext());
        }

        return result;
    }


    public boolean deleteRoomUtility(long roomId, long utilityId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_ID.getColName() + " = ?"
                + RoomUtility.Column.COLUMN_UTILITY_ID.getColName() + " = ?",
                new String[]{String.valueOf(roomId), String.valueOf(utilityId)});
        return rowAffected > 0;
    }

    public boolean deleteRoomUtilityByRoomId(long roomId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(RoomUtility.TABLE_NAME,
                RoomUtility.Column.COLUMN_ROOM_ID.getColName() + " = ?",
                new String[]{String.valueOf(roomId)});
        return rowAffected > 0;
    }

    public boolean updateRoomUtility(RoomUtility newRoomUtility) {
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
