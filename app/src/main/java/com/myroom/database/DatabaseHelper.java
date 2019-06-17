package com.myroom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myroom.core.Constant;
import com.myroom.database.dao.BaseModel;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Room;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "myroomdb";
    private static final int DATABASE_VERSION = 1;

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
        
        createUtilityData(db);
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
        db.execSQL("INSERT INTO utility (id, utility_name) VALUES (1, \"Electricity\")");
        db.execSQL("INSERT INTO utility (id, utility_name) VALUES (2, \"Water\")");
        db.execSQL("INSERT INTO utility (id, utility_name) VALUES (3, \"Cab\")");
        db.execSQL("INSERT INTO utility (id, utility_name) VALUES (4, \"Internet\")");
    }

    private String dropTable(String tableRoom) {
        return "DROP TABLE IF EXISTS " + tableRoom;
    }

    private String createRoomTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(BaseModel.Column.COLUMN_ID.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Room.Column.COLUMN_ROOM_NAME.getColName(), TEXT));
        return buildCreateTableQuery(Room.TABLE_NAME, tableStructures);
    }

    private String createGuestTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(BaseModel.Column.COLUMN_ID.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Guest.Column.COLUMN_GUEST_NAME.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_BIRTH_DATE.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_ID_CARD.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_PHONE_NUMBER.getColName(), TEXT));
        tableStructures.add(createColumn(Guest.Column.COLUMN_ROOM_ID.getColName(), INTEGER));
        return buildCreateTableQuery(Guest.TABLE_NAME, tableStructures);
    }

    private String createUtilityTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(BaseModel.Column.COLUMN_ID.getColName(), INTEGER_PRIMARY_KEY));
        tableStructures.add(createColumn(Utility.Column.COLUMN_UTILITY_NAME.getColName(), TEXT));
        return buildCreateTableQuery(Utility.TABLE_NAME, tableStructures);
    }

    private String createRoomUtilityTable() {
        List<String> tableStructures = new ArrayList<>();
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_ROOM_ID.getColName(), INTEGER));
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_UTILITY_ID.getColName(), INTEGER));
        tableStructures.add(createColumn(RoomUtility.Column.COLUMN_UTILITY_FEE.getColName(),TEXT));
        tableStructures.add(createForeignKey(RoomUtility.Column.COLUMN_ROOM_ID.getColName(), Room.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName()));
        tableStructures.add(createForeignKey(RoomUtility.Column.COLUMN_UTILITY_ID.getColName(), Utility.TABLE_NAME, BaseModel.Column.COLUMN_ID.getColName()));
        return buildCreateTableQuery(RoomUtility.TABLE_NAME, tableStructures);
    }

    private String createColumn(String name, String type) {
        return name + Constant.SPACE + type;
    }
    private String createForeignKey(String columnName, String foreignTable, String foreignColum) {
        return String.format("FOREIGN KEY (%s) REFERENCES %s (%s)", columnName, foreignTable, foreignColum);
    }

    private String buildCreateTableQuery(String tableName, List<String> tableStructure) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(tableName).append(" ( ");
        builder.append(StringUtils.join(tableStructure, Constant.COMMA)).append(" )");;
        return builder.toString();
    }
}
