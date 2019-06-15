package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.BaseModel;
import com.myroom.database.dao.Room;
import com.myroom.database.dao.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UtilityRepository implements IObjectRepository<Utility> {

    private DatabaseHelper dbHelper;

    @Inject
    public UtilityRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
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

        if (c.moveToFirst()) {
            Utility result = new Utility();
            result.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
            result.setName(c.getString(Utility.Column.COLUMN_UTILITY_NAME.getIndex()));
            return result;
        }
        return null;
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
