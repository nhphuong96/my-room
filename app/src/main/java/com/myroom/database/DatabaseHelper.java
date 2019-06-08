package com.myroom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myroom.core.Constant;
import com.myroom.model.BaseModel;
import com.myroom.model.Guest;
import com.myroom.model.Room;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "myroomdb";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";
    private static final String INTEGER_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";

    private static final String COLUMN_ID_NAME = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createGuestTable());
        db.execSQL(createRoomTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable(Guest.TABLE_NAME));
        db.execSQL(dropTable(Room.TABLE_NAME));
        onCreate(db);
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

    private String createColumn(String name, String type) {
        return name + Constant.SPACE + type;
    }

    private String buildCreateTableQuery(String tableName, List<String> tableStructure) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(tableName).append(" ( ");
        builder.append(StringUtils.join(tableStructure, Constant.COMMA)).append(" )");;
        return builder.toString();
    }
}
