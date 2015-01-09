package com.intelworkout.intelworkout;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xavier on 09/01/15.
 */
public class MatricesDataSource {
    // Base de données
    private SQLiteDatabase database;

    // Class table
    private MySQLiteMatrice dbMatrice;

    // Colonnes
    private String[] allColMatrice = { MySQLiteMatrice.COLUMN_ID,
            MySQLiteMatrice.COLUMN_MATRICE, MySQLiteMatrice.COLUMN_PSEUDO,
            MySQLiteMatrice.COLUMN_TIME };

    public MatricesDataSource(Context context) {
        dbMatrice = new MySQLiteMatrice(context);
    }

    public void open() {
        Log.i("openMatrice", "openMatrice");
        try {
            database = dbMatrice.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("Erreur Ouverture Matrice : ", e.toString());
        }
    }

    public void close() {
        Log.i("closeMatrice", "close Matrice");
        dbMatrice.close();

    }

    public void addData(String matrice, String pseudo, String temps) {
        Log.i("Class MatricesDataSource", "addData");
        open();

        String sql = "INSERT INTO " + dbMatrice.TABLE + " (" + allColMatrice[1]
                + ", " + allColMatrice[2] + ", " + allColMatrice[3]
                + ") VALUES (?,?,?)";
        database.execSQL(sql, new String[] { matrice, pseudo, temps });

        close();
    }

    public void dropTableMatrice() {
        Log.i("Class MatricesDataSource", "dropTableMatrice1");
        dbMatrice.onDrop(database);
    }

    public List<MatriceData> fctRecupDatas() {
        List<MatriceData> lstDatas = new ArrayList<MatriceData>();

        String request = "SELECT * FROM " + dbMatrice.TABLE + " ORDER BY " + allColMatrice[1] + " ASC";

        Cursor cursor = database.rawQuery(request, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MatriceData dataMatrice = cursorToData(cursor);
            lstDatas.add(dataMatrice);
            cursor.moveToNext();
        }
        cursor.close();



        return lstDatas;
    }

    private MatriceData cursorToData(Cursor cursor) {
        MatriceData comment = new MatriceData();
        comment.setId(cursor.getLong(0));
        comment.setMatrice(cursor.getString(1));
        comment.setPseudo(cursor.getString(2));
        comment.setTime(cursor.getString(3));
        return comment;
    }
}
