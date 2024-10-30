package com.example.logbook_todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "StevilTodo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "stevil_todo";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK = "task_name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CHECKED = "is_checked";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_CHECKED + " INTEGER DEFAULT 0)";
        db.execSQL(query);
    }

    void checkStatus(String row_id, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Set the checked status: 1 for checked, 0 for unchecked
        cv.put(COLUMN_CHECKED, isChecked ? 1 : 0);

        // Update the row in the database where the ID matches
        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{row_id});

        // Check the result of the update operation
        if (result == -1) {
            Toast.makeText(context, "Error updating task status", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Task status updated!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addTask(String task_name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK, task_name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_CHECKED, 0);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String task_name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK, task_name);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTaskStatus(String taskId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Use integer value 0 or 1 for checked status
        contentValues.put(COLUMN_CHECKED, status.equals("1") ? 1 : 0);

        // Ensure you use the correct column name for the ID
        int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{taskId});

        if (result == -1) {
            Toast.makeText(context, "Failed to Update Task Status!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Task Status Updated!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }



    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
