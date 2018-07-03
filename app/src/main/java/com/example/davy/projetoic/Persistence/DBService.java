package com.example.davy.projetoic.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;

public class DBService {
    private SQLiteDatabase db;
    public static final int ID = 0;
    public static final int EMAIL = 1;
    public static final int TOKEN = 2;
    public static final int NAME = 3;

    public DBService(Context contxt) {
        BDcache cache = new BDcache(contxt);
        this.db = cache.getWritableDatabase();
    }

    public void insert(String email, String token, String name){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("", token);
        values.put("name", name);

        db.insert(BDcache.TABLE, null, values);
    }

    public int update(String email, String token, String name){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("token", token);
        values.put("name", name);

        return db.update(BDcache.TABLE, values, "_id = 1", null);
    }


    /**
     * Retorna o único usuário corrente possivel
     * @return index (0)_id, index(1) email and index(2) token from current user
     */
    public String[] getUser(){
        Cursor cursor = db.query(BDcache.TABLE, BDcache.COLUMS, null, null
                , null, null, null);
        cursor.moveToFirst();
        String[] result;
        if(cursor.isFirst())
            result = new String[]{cursor.getString(0), cursor.getString(1)
                , cursor.getString(2), cursor.getString(3)};
        else result = new String[0];
        cursor.close();
        return result;
    }
}
