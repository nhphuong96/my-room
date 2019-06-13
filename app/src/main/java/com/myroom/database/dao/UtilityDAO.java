package com.myroom.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.database.DatabaseHelper;
import com.myroom.model.BaseModel;
import com.myroom.model.Room;
import com.myroom.model.Utility;

import java.util.ArrayList;
import java.util.List;

public class UtilityDAO implements IObjectDAO<Utility> {

    private DatabaseHelper dbHelper;

    public UtilityDAO(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    @Override
    public long add(Utility entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.Column.COLUMN_UTILITY_NAME.getColName(), entity.getName());
        long id = db.insertOrThrow(Utility.TABLE_NAME, null, values);
        return id;
    }

    @Override
    public Utility find(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Utility.Column.COLUMN_UTILITY_NAME.getColName(),
        };
        Cursor c = db.query(Utility.TABLE_NAME, columns, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        Utility result = new Utility();
        result.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
        result.setName(c.getString(Utility.Column.COLUMN_UTILITY_NAME.getIndex()));
        return result;
    }

    @Override
    public List<Utility> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Utility.TABLE_NAME, null);

        List<Utility> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Utility utility = new Utility();
                utility.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
                utility.setName(c.getString(Room.Column.COLUMN_ROOM_NAME.getIndex()));
                result.add(utility);
            }
            while (c.moveToNext());
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Utility.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(id)});
        return rowAffected > 0;
    }

    @Override
    public boolean update(Utility entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.Column.COLUMN_UTILITY_NAME.getColName(), entity.getName());
        int rowAffected = db.update(Utility.TABLE_NAME, values, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(entity.getId())});
        return rowAffected > 0;
    }
}
