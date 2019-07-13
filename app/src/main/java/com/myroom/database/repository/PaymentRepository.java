package com.myroom.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myroom.application.BaseApplication;
import com.myroom.database.DatabaseHelper;
import com.myroom.database.dao.BaseModel;
import com.myroom.database.dao.Payment;
import com.myroom.exception.OperationException;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        values.put(Payment.Column.COLUMN_ROOM_ID.getColName(), entity.getRoomId());
        values.put(Payment.Column.COLUMN_CREATION_DATE.getColName(), new Date().toString());
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
    public Payment find(long id) {
        return null;
    }

    @Override
    public List<Payment> findAll() {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Payment entity) {
        return false;
    }

    public List<Payment> findPaymentByRoomId(long roomId) throws OperationException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[] {
                BaseModel.Column.COLUMN_ID.getColName(),
                Payment.Column.COLUMN_ROOM_ID.getColName(),
                Payment.Column.COLUMN_CREATION_DATE.getColName(),
                Payment.Column.COLUMN_PAYMENT_DATE.getColName(),
                Payment.Column.COLUMN_ELECTRICITY_FEE.getColName(),
                Payment.Column.COLUMN_WATER_FEE.getColName(),
                Payment.Column.COLUMN_CAB_FEE.getColName(),
                Payment.Column.COLUMN_INTERNET_FEE.getColName(),
                Payment.Column.COLUMN_ROOM_FEE.getColName()
        };
        Cursor c = db.query(Payment.TABLE_NAME, columns, Payment.Column.COLUMN_ROOM_ID.getColName() + " = ?", new String[] {String.valueOf(roomId)}, null, null, null, null);

        List<Payment> paymentsInRoom = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    Payment payment = new Payment();
                    payment.setId(Integer.parseInt(c.getString(BaseModel.Column.COLUMN_ID.getIndex())));
                    payment.setRoomId(Integer.valueOf(c.getString(Payment.Column.COLUMN_ROOM_ID.getIndex())));
                    payment.setCreationDate(sdf.parse(c.getString(Payment.Column.COLUMN_CREATION_DATE.getIndex())));
                    payment.setPaymentDate(sdf.parse(c.getString(Payment.Column.COLUMN_PAYMENT_DATE.getIndex())));
                    payment.setElectricityFee(c.getString(Payment.Column.COLUMN_ELECTRICITY_FEE.getIndex()));
                    payment.setWaterFee(c.getString(Payment.Column.COLUMN_WATER_FEE.getIndex()));
                    payment.setCabFee(c.getString(Payment.Column.COLUMN_CAB_FEE.getIndex()));
                    payment.setInternetFee(c.getString(Payment.Column.COLUMN_INTERNET_FEE.getIndex()));
                    payment.setRoomFee(c.getString(Payment.Column.COLUMN_ROOM_FEE.getIndex()));
                    paymentsInRoom.add(payment);
                }
                while (c.moveToNext());
            }
        } catch (ParseException e) {
            throw new OperationException(e.getMessage());
        }
        return paymentsInRoom;
    }
}
