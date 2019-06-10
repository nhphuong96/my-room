package com.myroom.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.database.DatabaseHelper;
import com.myroom.model.BaseModel;
import com.myroom.model.Utility;

import java.util.ArrayList;
import java.util.List;

public class UtilityDAO {

    private DatabaseHelper dbHelper;

    public UtilityDAO(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    public long addUtility(Utility utility) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.Column.COLUMN_UTILITY_NAME.getColName(), utility.getName());
        long id = db.insertOrThrow(Utility.TABLE_NAME, null, values);
        return id;
    }

    public Utility findUtility(long id) {
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

    public boolean deleteUtility(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Utility.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(id)});
        return rowAffected > 0;
    }

    public boolean updateUtility(Utility newUtility) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.Column.COLUMN_UTILITY_NAME.getColName(), newUtility.getName());
        int rowAffected = db.update(Utility.TABLE_NAME, values, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(newUtility.getId())});
        return rowAffected > 0;
    }
}
