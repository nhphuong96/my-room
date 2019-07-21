package com.myroom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myroom.adapter.UtilityInRoomAdapter;
import com.myroom.core.Constant;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Payment;
import com.myroom.database.dao.Room;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;
import com.myroom.database.dao.UtilityIndex;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "myroomdb";
    private static final int DATABASE_VERSION = 2;

    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";
    private static final String INTEGER_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createGuestTable());
        db.execSQL(createRoomTable());
        db.execSQL(createUtilityTable());
        db.execSQL(createRoomUtilityTable());
        db.execSQL(createCurrencyTable());
        db.execSQL(createPaymentTable());
        db.execSQL(createUtilityIndexTable());
        
        createUtilityData(db);
        createCurrencyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable(Guest.TABLE_NAME));
        db.execSQL(dropTable(Room.TABLE_NAME));
        db.execSQL(dropTable(Utility.TABLE_NAME));
        db.execSQL(dropTable(RoomUtility.TABLE_NAME));
        onCreate(db);
    }

    private void createUtilityData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO utility (utility_key, utility_id, utility_name, utility_icon) VALUES (1, \"ELECTRICITY\", \"Điện\", \"ic_electricity\")");
        db.execSQL("INSERT INTO utility (utility_key, utility_id, utility_name, utility_icon) VALUES (2, \"WATER\", \"Nước\", \"ic_water\")");
        db.execSQL("INSERT INTO utility (utility_key, utility_id, utility_name, utility_icon) VALUES (3, \"CAB\", \"Cab\", \"ic_tv\")");
        db.execSQL("INSERT INTO utility (utility_key, utility_id, utility_name, utility_icon) VALUES (4, \"INTERNET\", \"Internet\", \"ic_internet\")");
        db.execSQL("INSERT INTO utility (utility_key, utility_id, utility_name, utility_icon) VALUES (5, \"ROOM\", \"Phí thuê phòng\", \"ic_room\")");
    }

    private void createCurrencyData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO currency (currency_key, currency_id, currency_icon, is_selected) VALUES (1, \"VND\", \"ic_vietnam_flag\", 1)");
        db.execSQL("INSERT INTO currency (currency_key, currency_id, currency_icon, is_selected) VALUES (2, \"USD\", \"ic_american_flag\", 0)");
        db.execSQL("INSERT INTO currency (currency_key, currency_id, currency_icon, is_selected) VALUES (3, \"CNY\", \"ic_china_flag\", 0)");
        db.execSQL("INSERT INTO currency (currency_key, currency_id, currency_icon, is_selected) VALUES (4, \"JPY\", \"ic_japan_flag\", 0)");
        db.execSQL("INSERT INTO currency (currency_key, currency_id, currency_icon, is_selected) VALUES (5, \"EUR\", \"ic_europe_flag\", 0)");
    }

    private String dropTable(String tableRoom) {
        return "DROP TABLE IF EXISTS " + tableRoom;
    }

    private String createPaymentTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(Payment.Column.COLUMN_PAYMENT_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Payment.Column.COLUMN_ROOM_KEY.getColName(), INTEGER));
        tableStructures.add(createColumn(Payment.Column.COLUMN_CREATION_DATE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_PAYMENT_DATE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_ELECTRICITY_FEE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_WATER_FEE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_CAB_FEE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_INTERNET_FEE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_ROOM_FEE.getColName(), TEXT));
        tableStructures.add(createColumn(Payment.Column.COLUMN_IS_PAID.getColName(), INTEGER));
        return buildCreateTableQuery(Payment.TABLE_NAME, tableStructures);
    }

    private String createUtilityIndexTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(UtilityIndex.Column.COLUMN_UTILITY_INDEX_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(UtilityIndex.Column.COLUMN_PAYMENT_KEY.getColName(), INTEGER));
        tableStructures.add(createColumn(UtilityIndex.Column.COLUMN_UTILITY_KEY.getColName(), INTEGER));
        tableStructures.add(createColumn(UtilityIndex.Column.COLUMN_LAST_INDEX.getColName(), TEXT));
        tableStructures.add(createColumn(UtilityIndex.Column.COLUMN_CURRENT_INDEX.getColName(), TEXT));
        tableStructures.add(createForeignKey(UtilityIndex.Column.COLUMN_PAYMENT_KEY.getColName(), Payment.TABLE_NAME, Payment.Column.COLUMN_PAYMENT_KEY.getColName()));
        tableStructures.add(createForeignKey(UtilityIndex.Column.COLUMN_UTILITY_KEY.getColName(), Utility.TABLE_NAME, Utility.Column.COLUMN_UTILITY_KEY.getColName()));
        return buildCreateTableQuery(UtilityIndex.TABLE_NAME, tableStructures);
    }

    private String createCurrencyTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(Currency.Column.COLUMN_CURRENCY_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Currency.Column.COLUMN_CURRENCY_ID.getColName(), TEXT));
        tableStructures.add(createColumn(Currency.Column.COLUMN_CURRENCY_ICON.getColName(), TEXT));
        tableStructures.add(createColumn(Currency.Column.COLUMN_IS_SELECTED.getColName(), INTEGER));
        return buildCreateTableQuery(Currency.TABLE_NAME, tableStructures);
    }

    private String createRoomTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(Room.Column.COLUMN_ROOM_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Room.Column.COLUMN_ROOM_NAME.getColName(), TEXT));
        return buildCreateTableQuery(Room.TABLE_NAME, tableStructures);
    }

    private String createGuestTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(Guest.Column.COLUMN_GUEST_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Guest.Column.COLUMN_GUEST_NAME.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_BIRTH_DATE.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_ID_CARD.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_ROOM_ID.getColName(), INTEGER));
        tableStructures.add(createColumn(Guest.Column.COLUMN_GENDER.getColName(), INTEGER));
        return buildCreateTableQuery(Guest.TABLE_NAME, tableStructures);
    }

    private String createUtilityTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(Utility.Column.COLUMN_UTILITY_KEY.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Utility.Column.COLUMN_UTILITY_ID.getColName(), TEXT));
        tableStructures.add(createColumn(Utility.Column.COLUMN_UTILITY_NAME.getColName(), TEXT));
        tableStructures.add(createColumn(Utility.Column.COLUMN_UTILITY_ICON.getColName(), TEXT));
        return buildCreateTableQuery(Utility.TABLE_NAME, tableStructures);
    }

    private String createRoomUtilityTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_ROOM_KEY.getColName(), INTEGER));
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(), INTEGER));
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(),TEXT));
        tableStructures.add(createForeignKey(RoomUtility.Column.COLUMN_ROOM_KEY.getColName(), Room.TABLE_NAME, Room.Column.COLUMN_ROOM_KEY.getColName()));
        tableStructures.add(createForeignKey(RoomUtility.Column.COLUMN_UTILITY_KEY.getColName(), Utility.TABLE_NAME, Utility.Column.COLUMN_UTILITY_KEY.getColName()));
        return buildCreateTableQuery(RoomUtility.TABLE_NAME, tableStructures);
    }

    private String createColumn(String name, String type) {
        return name + Constant.SPACE + type;
    }
    private String createForeignKey(String columnName, String foreignTable, String foreignColumn) {
        return String.format("FOREIGN KEY (%s) REFERENCES %s (%s)", columnName, foreignTable, foreignColumn);
    }

    private String buildCreateTableQuery(String tableName, List<String> tableStructure) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(tableName).append(" ( ");
        builder.append(StringUtils.join(tableStructure, Constant.COMMA)).append(" )");;
        return builder.toString();
    }
}
