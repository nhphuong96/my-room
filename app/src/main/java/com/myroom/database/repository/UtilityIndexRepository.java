package com.myroom.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.Room;
import com.myroom.database.dao.Utility;
import com.myroom.database.dao.UtilityIndex;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UtilityIndexRepository implements IObjectRepository<UtilityIndex> {
    private DatabaseHelper dbHelper;
    @Inject
    public UtilityIndexRepository() {
        Context context = BaseApplication.getContextComponent().getContext();
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public long add(UtilityIndex entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UtilityIndex.Column.COLUMN_PAYMENT_KEY.getColName(), entity.getPaymentKey());
        values.put(UtilityIndex.Column.COLUMN_UTILITY_KEY.getColName(), entity.getUtilityKey());
        values.put(UtilityIndex.Column.COLUMN_LAST_INDEX.getColName(), entity.getLastIndex());
        values.put(UtilityIndex.Column.COLUMN_CURRENT_INDEX.getColName(), entity.getCurrentIndex());
        long id = db.insertOrThrow(UtilityIndex.TABLE_NAME, null, values);
        return id;
    }

    @Override
    public UtilityIndex find(long key) {
        return null;
    }

    public List<UtilityIndex> findUtilityIndexByPaymentKey(long paymentKey) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                UtilityIndex.Column.COLUMN_UTILITY_INDEX_KEY.getColName(),
                UtilityIndex.Column.COLUMN_PAYMENT_KEY.getColName(),
                UtilityIndex.Column.COLUMN_UTILITY_KEY.getColName(),
                UtilityIndex.Column.COLUMN_LAST_INDEX.getColName(),
                UtilityIndex.Column.COLUMN_CURRENT_INDEX.getColName(),
        };
        Cursor c = db.query(UtilityIndex.TABLE_NAME, columns, UtilityIndex.Column.COLUMN_PAYMENT_KEY.getColName() + " = ?", new String[] {String.valueOf(paymentKey)}, null, null, null, null);
        List<UtilityIndex> utilityIndices = null;
        if (c.moveToFirst()) {
            utilityIndices = new ArrayList<>();
            do {
                UtilityIndex result = new UtilityIndex();
                result.setUtilityIndexKey(Integer.valueOf(c.getString(UtilityIndex.Column.COLUMN_UTILITY_INDEX_KEY.getIndex())));
                result.setPaymentKey(Integer.valueOf(c.getString(UtilityIndex.Column.COLUMN_PAYMENT_KEY.getIndex())));
                result.setUtilityKey(Integer.valueOf(c.getString(UtilityIndex.Column.COLUMN_UTILITY_KEY.getIndex())));
                result.setLastIndex(c.getString(UtilityIndex.Column.COLUMN_LAST_INDEX.getIndex()));
                result.setCurrentIndex(c.getString(UtilityIndex.Column.COLUMN_CURRENT_INDEX.getIndex()));
                utilityIndices.add(result);
            }
            while (c.moveToNext());

        }
        return utilityIndices;
    }

    @Override
    public List<UtilityIndex> findAll() {
        return null;
    }

    @Override
    public boolean delete(long key) {
        return false;
    }

    @Override
    public boolean update(UtilityIndex entity) {
        return false;
    }
}
