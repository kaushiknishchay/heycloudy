package com.kaushik.heycloudy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SHolmes on 09-Jul-17.
 */

public class PathsHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "backup_paths";
    public static final String COLUMN_PATH = "path";

    public static final String COLUMN_ID = "_id";
    private static final String DATABASE_NAME = "heycloudy.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_NAME + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PATH + " text not null UNIQUE);";

    public PathsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Log.w("PATH-HELPER",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w("PATH-HELPER",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
