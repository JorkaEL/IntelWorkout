package com.intelworkout.intelworkout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by xavier on 09/01/15.
 */
public class MySQLiteMatrice {

    // SQL BDD
    static final String DATABASE_NAME = "bddIntellWork.db";
    static final int DATABASE_VERSION = 1;

    // SQL requete
    static final String TABLE = "MATRICE";
    static final String COLUMN_ID = "_ID";
    static final String COLUMN_MATRICE = "_MATRICE";
    static final String COLUMN_PSEUDO = "_PSEUDO";
    static final String COLUMN_TIME = "_TIME";

    public MySQLiteMatrice(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    private static final String DATABASE_CREATE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MATRICE + " text not null, " + COLUMN_PSEUDO
            + " text not null," + COLUMN_TIME + " text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Class MySQLiteMatrice1", "Fct : onCreate");
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Class MySQLiteMatrice1", "Fct : onDrop");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE + ";");
        onCreate(db);
    }

    public void onDrop(SQLiteDatabase db) {
        Log.d("Class MySQLiteMatrice1", "Fct : onDrop");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE + ";");
        onCreate(db);
    }

}
