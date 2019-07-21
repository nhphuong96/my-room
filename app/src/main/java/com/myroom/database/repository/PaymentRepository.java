package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Payment;
import com.myroom.exception.OperationException;
import com.myroom.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PaymentRepository implements IObjectRepository<Payment> {

    private DatabaseHelper dbHelper;

    @Inject
    public PaymentRepository() {
        dbHelper = new DatabaseHelper(BaseApplication.getContextComponent().getContext());
    }

    @Override
    public long add(Payment entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Payment.Column.COLUMN_ROOM_KEY.getColName(), entity.getRoomKey());
        values.put(Payment.Column.COLUMN_CREATION_DATE.getColName(), DateUtils.convertDateToStringAsDDMMYYYYHHMMSS(entity.getCreationDate()));
        values.put(Payment.Column.COLUMN_PAYMENT_DATE.getColName(), StringUtils.EMPTY);
        values.put(Payment.Column.COLUMN_ELECTRICITY_FEE.getColName(), entity.getElectricityFee());
        values.put(Payment.Column.COLUMN_WATER_FEE.getColName(), entity.getWaterFee());
        values.put(Payment.Column.COLUMN_CAB_FEE.getColName(), entity.getCabFee());
        values.put(Payment.Column.COLUMN_INTERNET_FEE.getColName(), entity.getInternetFee());
        values.put(Payment.Column.COLUMN_ROOM_FEE.getColName(), entity.getRoomFee());
        long id = db.insertOrThrow(Payment.TABLE_NAME, null, values);
        return id;
    }

    @Override
    public Payment find(long key) {
        return null;
    }

    @Override
    public List<Payment> findAll() {
        return null;
    }

    @Override
    public boolean delete(long key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Payment.TABLE_NAME, Payment.Column.COLUMN_PAYMENT_KEY.getColName() + " = ?", new String[]{String.valueOf(key)});
        return rowAffected > 0;
    }

    public boolean deleteByRoom(long roomKey) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowAffected = db.delete(Payment.TABLE_NAME, Payment.Column.COLUMN_ROOM_KEY.getColName() + " = ?", new String[]{String.valueOf(roomKey)});
        return rowAffected > 0;
    }

    @Override
    public boolean update(Payment entity) {
        return false;
    }

    public List<Payment> findPaymentByRoomId(long roomId) throws OperationException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                Payment.Column.COLUMN_PAYMENT_KEY.getColName(),
                Payment.Column.COLUMN_ROOM_KEY.getColName(),
                Payment.Column.COLUMN_CREATION_DATE.getColName(),
                Payment.Column.COLUMN_PAYMENT_DATE.getColName(),
                Payment.Column.COLUMN_ELECTRICITY_FEE.getColName(),
                Payment.Column.COLUMN_WATER_FEE.getColName(),
                Payment.Column.COLUMN_CAB_FEE.getColName(),
                Payment.Column.COLUMN_INTERNET_FEE.getColName(),
                Payment.Column.COLUMN_ROOM_FEE.getColName()
        };
        Cursor c = db.query(Payment.TABLE_NAME, columns, Payment.Column.COLUMN_ROOM_KEY.getColName() + " = ?", new String[] {String.valueOf(roomId)}, null, null, null, null);

        List<Payment> paymentsInRoom = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setPaymentKey(Integer.parseInt(c.getString(Payment.Column.COLUMN_PAYMENT_KEY.getIndex())));
                payment.setRoomKey(Integer.valueOf(c.getString(Payment.Column.COLUMN_ROOM_KEY.getIndex())));
                payment.setCreationDate(DateUtils.convertStringToDateAsDDMMYYYYHHMMSS(c.getString(Payment.Column.COLUMN_CREATION_DATE.getIndex())));
                payment.setPaymentDate(DateUtils.convertStringToDateAsDDMMYYYYHHMMSS(c.getString(Payment.Column.COLUMN_PAYMENT_DATE.getIndex())));
                payment.setElectricityFee(c.getString(Payment.Column.COLUMN_ELECTRICITY_FEE.getIndex()));
                payment.setWaterFee(c.getString(Payment.Column.COLUMN_WATER_FEE.getIndex()));
                payment.setCabFee(c.getString(Payment.Column.COLUMN_CAB_FEE.getIndex()));
                payment.setInternetFee(c.getString(Payment.Column.COLUMN_INTERNET_FEE.getIndex()));
                payment.setRoomFee(c.getString(Payment.Column.COLUMN_ROOM_FEE.getIndex()));
                paymentsInRoom.add(payment);
            }
            while (c.moveToNext());
        }
        return paymentsInRoom;
    }
}
