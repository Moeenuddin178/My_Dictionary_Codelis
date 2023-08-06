package com.my.dictionary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.dictionary.R;
import com.my.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUserManager extends SQLiteOpenHelper {


    public static int DB_USER_VERSION = 1;
    public static String DB_USER_NAME = "database_user.sqlite";

    private Context context;
    public DatabaseUserManager(Context context) {
        super(context, DB_USER_NAME, null, DB_USER_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable1User(db);
        createTable2User(db);
    }

    private void createTable1User(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + getTABLE1_NAME() + " ("
                + getCOL1_EDITED() + " TEXT, "
                + getCOL2_FAVORITES() + " TEXT, "
                + getCOL3_ID() + " INTEGER PRIMARY KEY, "
                + getCOL4_WORD() + " TEXT, "
                + getCOL5_RESULT() + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    private void createTable2User(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + getTABLE2_NAME() + " ("
                + getCOL1_EDITED() + " TEXT, "
                + getCOL2_FAVORITES() + " TEXT, "
                + getCOL3_ID() + " INTEGER PRIMARY KEY, "
                + getCOL4_WORD() + " TEXT, "
                + getCOL5_RESULT() + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE);
    }



    public List<Word> getAllRow(String table) {
        SQLiteDatabase db = getWritableDatabase();
        List<Word> wordList = new ArrayList<>();
        Cursor cur;
        try {
            cur = db.rawQuery("SELECT * FROM " + table, null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    Word w = new Word();
                    w.setEdited(cur.getString(0));
                    w.setFavorites(cur.getString(1));
                    w.setId(cur.getInt(2)+"");
                    w.setWord(cur.getString(3));
                    w.setResult(cur.getString(4));
                    w.setUserDb(true);
                    wordList.add(w);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
        }
        return wordList;
    }

    public void updateRecord(String table, Word word) {
        String updateQuery = "UPDATE " + table + " SET "
                + getCOL4_WORD() + "='" + word.getWord() + "', "
                + getCOL5_RESULT() + "='" + word.getResult() + "', "
                + getCOL1_EDITED() + "='" + word.getEdited() + "' "
                + "WHERE " + getCOL3_ID() + "=" + word.getId();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateQuery);
        db.close();
    }

    public void insertNewRecord(String table, Word word) {
        String insertQuery = "INSERT INTO " + table
                + "(" + getCOL1_EDITED() + ", " + getCOL2_FAVORITES() + ", " + getCOL4_WORD() + ", " + getCOL5_RESULT() + ") "
                + " VALUES ('" + word.getEdited() + "', '" + word.getFavorites() + "', '" + word.getWord() + "', '" + word.getResult() + "' )";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(insertQuery);
        db.close();
    }

    public void deleteRecord(String table, Word word) {
        String deleteQuery = "DELETE FROM " + table + " WHERE " + getCOL3_ID() + "=" + word.getId();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

    private int getTable1Size() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(" + getCOL3_ID() + ") FROM " + getTABLE1_NAME(), null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    private int getTable2Size() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(" + getCOL3_ID() + ") FROM " + getTABLE2_NAME(), null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getMyWordSize() {
        return getTable1Size() + getTable2Size();
    }

    public String getCOL1_EDITED() {
        return (context.getResources().getString(R.string.column1_edited));
    }

    public String getCOL2_FAVORITES() {
        return (context.getResources().getString(R.string.column2_favorites));
    }

    public String getCOL3_ID() {
        return (context.getResources().getString(R.string.column3_id));
    }

    public String getCOL4_WORD() {
        return (context.getResources().getString(R.string.column4_word));
    }

    public String getCOL5_RESULT() {
        return (context.getResources().getString(R.string.column5_result));
    }

    public String getTABLE1_NAME() {
        return (context.getResources().getString(R.string.table1_ind_en));
    }

    public String getTABLE2_NAME() {
        return (context.getResources().getString(R.string.table2_en_ind));
    }
}
