package com.example.davy.projetoic.Persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDcache extends SQLiteOpenHelper {
    public static final String DB_NAME = "DBCache";
    public static final int DB_VERSION = 5;
    public static final String TABLE = "user";
    public static  final String[] COLUMS = new String[]{"_id", "email", "token", "name"};


    public BDcache(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+"(_id INTEGER PRIMARY KEY AUTOINCREMENT"+
                ", email TEXT NOT NULL, token TEXT NOT NULL, name TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE);
        onCreate(db);
    }
}
