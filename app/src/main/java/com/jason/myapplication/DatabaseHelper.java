package com.jason.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Assessments.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "assessments";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_EXERCISE_FREQUENCY = "exerciseFrequency";
    private static final String COLUMN_ENERGY_LEVELS = "energyLevels";
    private static final String COLUMN_SLEEP_QUALITY = "sleepQuality";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ASSESSMENTS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_EXERCISE_FREQUENCY + " TEXT, " +
                COLUMN_ENERGY_LEVELS + " INTEGER, " +
                COLUMN_SLEEP_QUALITY + " INTEGER)";
        db.execSQL(CREATE_ASSESSMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertAssessment(float weight, float height, String exerciseFrequency, int energyLevels, int sleepQuality) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_WEIGHT + ", " +
                COLUMN_HEIGHT + ", " +
                COLUMN_EXERCISE_FREQUENCY + ", " +
                COLUMN_ENERGY_LEVELS + ", " +
                COLUMN_SLEEP_QUALITY + ") VALUES (" +
                weight + ", " + height + ", '" + exerciseFrequency + "', " +
                energyLevels + ", " + sleepQuality + ")";
        db.execSQL(insertQuery);
        db.close();
    }

    public Cursor getLastAssessment() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        return db.rawQuery(selectQuery, null);
    }
}
