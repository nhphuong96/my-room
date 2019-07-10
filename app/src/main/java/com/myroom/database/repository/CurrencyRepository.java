package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.BaseModel;
import com.myroom.database.dao.Currency;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CurrencyRepository implements  IObjectRepository<Currency> {

    private DatabaseHelper dbHelper;

    @Inject
    public CurrencyRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
    }
    @Override
    public long add(Currency entity) {
        return 0;
    }

    @Override
    public Currency find(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Currency.Column.COLUMN_CURRENCY_CD.getColName(),
                Currency.Column.COLUMN_CURRENCY_ICON.getColName(),
                Currency.Column.COLUMN_IS_SELECTED.getColName()
        };
        Cursor c = db.query(Currency.TABLE_NAME, columns, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (c.moveToFirst()) {
            Currency result = new Currency();
            result.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
            result.setCurrencyCd(c.getString(Currency.Column.COLUMN_CURRENCY_CD.getIndex()));
            result.setCurrencyIcon(c.getString(Currency.Column.COLUMN_CURRENCY_ICON.getIndex()));
            result.setIsSelected(Integer.valueOf(c.getString(Currency.Column.COLUMN_IS_SELECTED.getIndex())));
            return result;
        }
        return null;
    }

    @Override
    public List<Currency> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Currency.TABLE_NAME, null);

        List<Currency> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Currency currency = new Currency();
                currency.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
                currency.setCurrencyCd(c.getString(Currency.Column.COLUMN_CURRENCY_CD.getIndex()));
                currency.setCurrencyIcon(c.getString(Currency.Column.COLUMN_CURRENCY_ICON.getIndex()));
                currency.setIsSelected(Integer.valueOf(c.getString(Currency.Column.COLUMN_IS_SELECTED.getIndex())));
                result.add(currency);
            }
            while (c.moveToNext());
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Currency entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Currency.Column.COLUMN_CURRENCY_CD.getColName(), entity.getCurrencyCd());
        values.put(Currency.Column.COLUMN_CURRENCY_ICON.getColName(), entity.getCurrencyIcon());
        values.put(Currency.Column.COLUMN_IS_SELECTED.getColName(), entity.getIsSelected());
        int rowAffected = db.update(Currency.TABLE_NAME, values, BaseModel.Column.COLUMN_ID.getColName() + " = ?", new String[]{String.valueOf(entity.getId())});
        return rowAffected > 0;

    }
}
