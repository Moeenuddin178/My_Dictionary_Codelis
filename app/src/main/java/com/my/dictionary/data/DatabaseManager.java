package com.my.dictionary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.my.dictionary.R;
import com.my.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(context, getDB_NAME(), getDBVersion());
        db = dbHelper.openDataBase();
    }


    public List<Word> getAllRow(String keyword, String table, boolean limit) {
        List<Word> wordList = new ArrayList<>();
        Cursor cur;
        try {
            if (limit) {
                cur = db.rawQuery("SELECT * FROM " + table + " WHERE " + getCOL4_WORD() + " LIKE '" + keyword + "%' LIMIT 15", null);
            } else {
                cur = db.rawQuery("SELECT * FROM " + table + " WHERE " + getCOL4_WORD() + " LIKE '" + keyword + "%'", null);
            }

            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    Word w = new Word();
                    w.setEdited(cur.getString(0));
                    w.setFavorites(cur.getString(1));
                    w.setId(cur.getString(2));
                    w.setWord(cur.getString(3));
                    w.setResult(cur.getString(4));
                    w.setUserDb(false);
                    wordList.add(w);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
        }
        return wordList;
    }

    public void updateFavorites(String table, Word word) {
        String updateQuery = "UPDATE " + table + " SET favorites='" + word.getFavorites() + "' WHERE _id=" + word.getId();
        db.execSQL(updateQuery);
    }

    public List<Word> getFavoritesRow() {
        List<Word> wordList = new ArrayList<>();
        Cursor cur;
        try {
            cur = db.rawQuery("SELECT * FROM " + getTABLE1_NAME() + " WHERE " + getCOL2_FAVORITES() + "='true'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    Word w = new Word();
                    w.setEdited(cur.getString(0));
                    w.setFavorites(cur.getString(1));
                    w.setId(cur.getString(2));
                    w.setWord(cur.getString(3));
                    w.setResult(cur.getString(4));
                    w.setUserDb(false);
                    wordList.add(w);
                } while (cur.moveToNext());
            }

            cur = db.rawQuery("SELECT * FROM " + getTABLE2_NAME() + " WHERE " + getCOL2_FAVORITES() + "='true'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    Word w = new Word();
                    w.setEdited(cur.getString(0));
                    w.setFavorites(cur.getString(1));
                    w.setId(cur.getString(2));
                    w.setWord(cur.getString(3));
                    w.setResult(cur.getString(4));
                    w.setUserDb(false);
                    wordList.add(w);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
        }
        return wordList;
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


    public String getDB_NAME() {
        return (context.getResources().getString(R.string.db_name));
    }


    public String getTABLE1_NAME() {
        return (context.getResources().getString(R.string.table1_ind_en));
    }


    public String getTABLE2_NAME() {
        return (context.getResources().getString(R.string.table2_en_ind));
    }

    public int getDBVersion() {
        return (context.getResources().getInteger(R.integer.db_version));
    }
}
