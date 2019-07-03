package com.myroom.database.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.BaseModel;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.Room;
import com.myroom.database.dao.Utility;

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
                currency.setIsDecimal(Integer.valueOf(c.getString(Currency.Column.COLUMN_IS_DECIMAL.getIndex())));
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
        return false;
    }
}
