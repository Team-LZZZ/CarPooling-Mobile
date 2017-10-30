package edu.wpi.cs528.lzzz.carpooling_mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;

/**
 * Created by QQZhao on 10/28/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "carpooling.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Tables.UserTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Tables.UserTable.Cols.UUID + ", " +
                Tables.UserTable.Cols.USERNAME + ", " +
                Tables.UserTable.Cols.PASSWORD +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUser(User user){
        
    }
}
